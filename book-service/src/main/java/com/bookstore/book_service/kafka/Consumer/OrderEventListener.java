package com.bookstore.book_service.kafka.Consumer;

import com.bookstore.book_service.entity.Book;
import com.bookstore.book_service.event.BookStockEvent;
import com.bookstore.book_service.event.OrderEvent;
import com.bookstore.book_service.repository.BookRepository;
import com.bookstore.book_service.kafka.Producer.BookStockProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final BookRepository bookRepository;
    private final BookStockProducer bookStockProducer;

    @KafkaListener(topics = "order-topic", groupId = "book-service-group")
    public void consume(OrderEvent event) {
        log.info("Received Order Event: {}", event);
        BookStockEvent response = new BookStockEvent();
        response.setOrderId(event.getOrderId());
        response.setBookId(event.getBookId());

        bookRepository.findById(event.getBookId()).ifPresentOrElse(book -> {
            try {
                book.decreaseStock(event.getQuantity());
                bookRepository.save(book);
                response.setStockUpdated(true);
                response.setMessage("Stock reduced");

                log.info("Stock reduced for book ID {}. New stock: {}", book.getId(), book.getStock());
            } catch (IllegalArgumentException e) {
                response.setStockUpdated(false);
                response.setMessage("Insufficient stock");

                log.warn("Insufficient stock for book ID {}. Requested: {}, Available: {}",
                        book.getId(), event.getQuantity(), book.getStock());
            }
        }, () -> {
            response.setStockUpdated(false);
            response.setMessage("Book not found");

            log.error("Book not found for ID {}", event.getBookId());
        });

        bookStockProducer.sendBookStockEvent(response);
        log.info("Sent BookStockEvent: {}", response);

    }
}
