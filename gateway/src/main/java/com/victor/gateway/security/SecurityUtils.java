package com.victor.gateway.security;

import com.victor.gateway.config.Constants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public static final String AUTHORITIES_KEY = "auth";

    private SecurityUtils() {}

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Mono<String> getCurrentUserLogin() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .flatMap(authentication -> Mono.justOrEmpty(extractPrincipal(authentication)));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Mono<String> getCurrentUserJWT() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static Mono<Boolean> isAuthenticated() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getAuthorities)
            .map(authorities -> authorities.stream().map(GrantedAuthority::getAuthority).noneMatch(AuthoritiesConstants.ANONYMOUS::equals));
    }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserAnyOfAuthorities(String... authorities) {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getAuthorities)
            .map(
                authorityList ->
                    authorityList
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(authority -> Arrays.asList(authorities).contains(authority))
            );
    }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserNoneOfAuthorities(String... authorities) {
        return hasCurrentUserAnyOfAuthorities(authorities).map(result -> !result);
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static Mono<Boolean> hasCurrentUserThisAuthority(String authority) {
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    public static Map<String, Object> extractDetailsFromTokenAttributes(Map<String, Object> attributes) {
        Map<String, Object> details = new HashMap<>();

        details.put("activated", Optional.ofNullable(attributes.get(StandardClaimNames.EMAIL_VERIFIED)).orElse(true));
        Optional.ofNullable(attributes.get("uid")).ifPresent(id -> details.put("id", id));
        Optional.ofNullable(attributes.get(StandardClaimNames.FAMILY_NAME)).ifPresent(lastName -> details.put("lastName", lastName));
        Optional.ofNullable(attributes.get(StandardClaimNames.PICTURE)).ifPresent(imageUrl -> details.put("imageUrl", imageUrl));

        Optional.ofNullable(attributes.get(StandardClaimNames.GIVEN_NAME)).ifPresentOrElse(
            firstName -> details.put("firstName", firstName),
            () -> Optional.ofNullable(attributes.get(StandardClaimNames.NAME)).ifPresent(firstName -> details.put("firstName", firstName))
        );

        if (attributes.get(StandardClaimNames.EMAIL) != null) {
            details.put("email", attributes.get(StandardClaimNames.EMAIL));
        } else {
            String sub = String.valueOf(attributes.get(StandardClaimNames.SUB));
            String preferredUsername = (String) attributes.get(StandardClaimNames.PREFERRED_USERNAME);
            if (sub.contains("|") && (preferredUsername != null && preferredUsername.contains("@"))) {
                // special handling for Auth0
                details.put("email", preferredUsername);
            } else {
                details.put("email", sub);
            }
        }

        if (attributes.get("langKey") != null) {
            details.put("langKey", attributes.get("langKey"));
        } else if (attributes.get(StandardClaimNames.LOCALE) != null) {
            // trim off country code if it exists
            String locale = (String) attributes.get(StandardClaimNames.LOCALE);
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            details.put("langKey", locale.toLowerCase());
        } else {
            // set langKey to default if not specified by IdP
            details.put("langKey", Constants.DEFAULT_LANGUAGE);
        }

        return details;
    }
}
