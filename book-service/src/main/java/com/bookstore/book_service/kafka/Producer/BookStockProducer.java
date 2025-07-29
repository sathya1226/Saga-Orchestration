package com.bookstore.book_service.kafka.Producer;

import com.bookstore.book_service.event.BookStockEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookStockProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topic.book-stock-events}")
    private String bookStockTopic;

    public void sendBookStockEvent(BookStockEvent event){
        log.info("Sending BookStockEvent to Kafka topic [{}]: {}", bookStockTopic, event);
        kafkaTemplate.send(bookStockTopic, event);

    }
}
