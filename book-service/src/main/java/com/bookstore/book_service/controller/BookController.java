package com.bookstore.book_service.controller;

import com.bookstore.book_service.dto.BookRequestDTO;
import com.bookstore.book_service.entity.Book;
import com.bookstore.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.createBook(dto));
    }

    @PutMapping("/{bookId}/stock")
    public ResponseEntity<Book> addStock(@PathVariable Long bookId, @RequestParam Integer stock) {
        return ResponseEntity.ok(bookService.addStock(bookId, stock));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
}
