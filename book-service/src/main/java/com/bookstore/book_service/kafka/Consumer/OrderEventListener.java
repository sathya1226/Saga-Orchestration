package com.bookstore.book_service.kafka.Consumer;

import com.bookstore.book_service.event.OrderEvent;
import com.bookstore.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final BookService bookService;

    @KafkaListener(topics = "${app.topic.order-events}", groupId = "book-service-group")
    public void listen(OrderEvent event){
        log.info("Received OrderEvent from Kafka: {}", event);
        bookService.processOrder(event.getOrderId(), event.getBookId());
    }
}
