package com.bookstore.book_service.serviceImpl;

import com.bookstore.book_service.dto.BookRequestDTO;
import com.bookstore.book_service.entity.Book;
import com.bookstore.book_service.repository.BookRepository;
import com.bookstore.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book createBook(BookRequestDTO dto) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();
        return bookRepository.save(book);
    }

    @Override
    public Book addStock(Long bookId, Integer stock) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setStock(book.getStock() + stock);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
