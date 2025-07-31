package com.bookstore.book_service.service;

import com.bookstore.book_service.dto.BookRequestDTO;
import com.bookstore.book_service.entity.Book;

import java.util.List;

public interface BookService {
    Book createBook(BookRequestDTO dto);
    Book addStock(Long bookId, Integer stock);
    List<Book> getAllBooks();
}
