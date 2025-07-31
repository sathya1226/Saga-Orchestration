package com.bookstore.book_service.kafka.Producer;

import com.bookstore.book_service.event.BookStockEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookStockProducer {

    private final KafkaTemplate<String, BookStockEvent> kafkaTemplate;

    public void sendBookStockEvent(BookStockEvent event) {
        log.info("Producing BookStockEvent to Kafka: {}", event);
        kafkaTemplate.send("book-stock-topic", event);
    }
}
