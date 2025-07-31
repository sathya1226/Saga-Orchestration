package com.bookstore.book_service.service;

import com.bookstore.book_service.dto.BookRequestDTO;
import com.bookstore.book_service.entity.Book;

import java.util.List;

public interface BookService {

    Book createBook(BookRequestDTO dto);

    List<Book> getAllBooks();

    Book getBookById(Long id);

    boolean processOrder(Long orderId, Long bookId);
}
