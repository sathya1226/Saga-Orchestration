package com.bookstore.book_service.service;

import com.bookstore.book_service.entity.Book;
import com.bookstore.common.dto.BookRequest;

public interface BookService {

    Book saveBook(Book book);

    String addStock(BookRequest request);

    boolean processOrder(Long orderId, Long bookId);

//    List<Book> getAllBooks();
//
//    Book getBookById(Long id);
//
//    boolean processOrder(Long orderId, Long bookId);
}
