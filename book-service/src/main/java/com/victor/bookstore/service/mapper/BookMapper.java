package com.victor.bookstore.service.mapper;

import com.victor.bookstore.domain.Book;
import com.victor.bookstore.service.dto.BookDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book> {}
