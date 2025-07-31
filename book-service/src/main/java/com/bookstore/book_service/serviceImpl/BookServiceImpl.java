package com.bookstore.book_service.serviceImpl;

import com.bookstore.book_service.dto.BookRequestDTO;
import com.bookstore.book_service.entity.Book;
import com.bookstore.book_service.event.BookStockEvent;
import com.bookstore.book_service.kafka.Producer.BookStockProducer;
import com.bookstore.book_service.repository.BookRepository;
import com.bookstore.book_service.service.BookService;
import com.bookstore.common.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookStockProducer bookStockProducer;

    @Override
    public Book createBook(BookRequestDTO dto){
        Book book = Book.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    @Transactional
    public boolean processOrder(Long orderId, Long bookId){
        Book book = bookRepository.findById(bookId).orElse(null);
        boolean available = false;

        if(book != null && book.getStock() > 0){
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);
            available = true;
        }

        BookStockEvent event = BookStockEvent.builder()
                .orderId(orderId)
                .bookId(bookId)
                .isStockAvailable(available)
                .orderStatus(available ? OrderStatus.CONFIRMED : OrderStatus.REJECTED)
                .eventType(available ? "BOOK_CONFIRMED" : "BOOK_REJECTED")
                .build();
        log.info("processing book stock for orderId: {} - Available: {}", orderId,available);
        bookStockProducer.sendBookStockEvent(event);
        return available;
    }
}
