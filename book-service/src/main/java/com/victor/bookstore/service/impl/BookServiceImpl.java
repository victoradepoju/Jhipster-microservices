package com.victor.bookstore.service.impl;

import com.victor.bookstore.domain.Book;
import com.victor.bookstore.repository.BookRepository;
import com.victor.bookstore.service.BookService;
import com.victor.bookstore.service.dto.BookDTO;
import com.victor.bookstore.service.mapper.BookMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.victor.bookstore.domain.Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        log.debug("Request to save Book : {}", bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDTO update(BookDTO bookDTO) {
        log.debug("Request to update Book : {}", bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public Optional<BookDTO> partialUpdate(BookDTO bookDTO) {
        log.debug("Request to partially update Book : {}", bookDTO);

        return bookRepository
            .findById(bookDTO.getId())
            .map(existingBook -> {
                bookMapper.partialUpdate(existingBook, bookDTO);

                return existingBook;
            })
            .map(bookRepository::save)
            .map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        log.debug("Request to get all Books");
        return bookRepository.findAll().stream().map(bookMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDTO> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }
}
