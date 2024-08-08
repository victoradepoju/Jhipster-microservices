package com.victor.bookstore.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.victor.bookstore.domain.Book} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String authorName;

    private String isbn;

    private Boolean archived;

    @NotNull
    private Boolean shareable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getShareable() {
        return shareable;
    }

    public void setShareable(Boolean shareable) {
        this.shareable = shareable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookDTO)) {
            return false;
        }

        BookDTO bookDTO = (BookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", authorName='" + getAuthorName() + "'" +
            ", isbn='" + getIsbn() + "'" +
            ", archived='" + getArchived() + "'" +
            ", shareable='" + getShareable() + "'" +
            "}";
    }
}
