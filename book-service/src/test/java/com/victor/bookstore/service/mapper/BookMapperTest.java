package com.victor.bookstore.service.mapper;

import static com.victor.bookstore.domain.BookAsserts.*;
import static com.victor.bookstore.domain.BookTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookSample1();
        var actual = bookMapper.toEntity(bookMapper.toDto(expected));
        assertBookAllPropertiesEquals(expected, actual);
    }
}
