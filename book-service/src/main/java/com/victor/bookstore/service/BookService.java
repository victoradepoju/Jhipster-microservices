package com.victor.bookstore.service;

import com.victor.bookstore.service.dto.BookDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.victor.bookstore.domain.Book}.
 */
public interface BookService {
    /**
     * Save a book.
     *
     * @param bookDTO the entity to save.
     * @return the persisted entity.
     */
    BookDTO save(BookDTO bookDTO);

    /**
     * Updates a book.
     *
     * @param bookDTO the entity to update.
     * @return the persisted entity.
     */
    BookDTO update(BookDTO bookDTO);

    /**
     * Partially updates a book.
     *
     * @param bookDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookDTO> partialUpdate(BookDTO bookDTO);

    /**
     * Get all the books.
     *
     * @return the list of entities.
     */
    List<BookDTO> findAll();

    /**
     * Get the "id" book.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookDTO> findOne(Long id);

    /**
     * Delete the "id" book.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
