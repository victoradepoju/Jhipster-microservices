package com.victor.bookstore.web.rest;

import com.victor.bookstore.repository.BookRepository;
import com.victor.bookstore.service.BookService;
import com.victor.bookstore.service.dto.BookDTO;
import com.victor.bookstore.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.victor.bookstore.domain.Book}.
 */
@RestController
@RequestMapping("/api/books")
public class BookResource {

    private static final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "bookServiceBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookService bookService;

    private final BookRepository bookRepository;

    public BookResource(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    /**
     * {@code POST  /books} : Create a new book.
     *
     * @param bookDTO the bookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookDTO, or with status {@code 400 (Bad Request)} if the book has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) throws URISyntaxException {
        log.debug("REST request to save Book : {}", bookDTO);
        if (bookDTO.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookDTO = bookService.save(bookDTO);
        return ResponseEntity.created(new URI("/api/books/" + bookDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookDTO.getId().toString()))
            .body(bookDTO);
    }

    /**
     * {@code PUT  /books/:id} : Updates an existing book.
     *
     * @param id the id of the bookDTO to save.
     * @param bookDTO the bookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookDTO,
     * or with status {@code 400 (Bad Request)} if the bookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookDTO bookDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Book : {}, {}", id, bookDTO);
        if (bookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookDTO = bookService.update(bookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookDTO.getId().toString()))
            .body(bookDTO);
    }

    /**
     * {@code PATCH  /books/:id} : Partial updates given fields of an existing book, field will ignore if it is null
     *
     * @param id the id of the bookDTO to save.
     * @param bookDTO the bookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookDTO,
     * or with status {@code 400 (Bad Request)} if the bookDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookDTO> partialUpdateBook(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookDTO bookDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Book partially : {}, {}", id, bookDTO);
        if (bookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookDTO> result = bookService.partialUpdate(bookDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /books} : get all the books.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of books in body.
     */
    @GetMapping("")
    public List<BookDTO> getAllBooks() {
        log.debug("REST request to get all Books");
        return bookService.findAll();
    }

    /**
     * {@code GET  /books/:id} : get the "id" book.
     *
     * @param id the id of the bookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable("id") Long id) {
        log.debug("REST request to get Book : {}", id);
        Optional<BookDTO> bookDTO = bookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookDTO);
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" book.
     *
     * @param id the id of the bookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
