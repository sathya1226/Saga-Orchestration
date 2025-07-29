package com.bookstore.book_service.controller;


import com.bookstore.book_service.entity.Book;
import com.bookstore.book_service.service.BookService;
import com.bookstore.common.dto.BookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<String> createBook(@RequestBody Book book){
        bookService.saveBook(book);
        return ResponseEntity.ok("Book Created");
    }

    @PostMapping("/add-stock")
    public ResponseEntity<String> addStock(@RequestBody BookRequest request){
        return ResponseEntity.ok(bookService.addStock(request));
    }

//    @GetMapping
//    public ResponseEntity<List<Book>> getAllBooks(){
//        return ResponseEntity.ok(bookService.getAllBooks());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Book> getBook(@PathVariable Long id){
//        return ResponseEntity.ok(bookService.getBookById(id));
//    }
}
