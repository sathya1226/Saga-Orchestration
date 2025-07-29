package com.bookstore.order_service.kafka.consumer;


import com.bookstore.order_service.event.BookStockEvent;
import com.bookstore.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookStockListener {

    private final OrderService orderService;

    @KafkaListener(topics = "book-stock-events", groupId = "order-service-group")
    public void listen(BookStockEvent stockEvent){
        System.out.println("Received stock event: " + stockEvent);
        orderService.updateOrderStatus(stockEvent.getOrderId(), stockEvent.getEventType());
    }
}
