package com.bookstore.payment_service.kafka.Consumer;

import com.bookstore.payment_service.event.OrderEvent;
import com.bookstore.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "book-stock-events", groupId = "payment-service-group")
    public void listen(OrderEvent orderEvent){
        if("BOOK_CONFIRMED".equals(orderEvent.getEventType())){
            System.out.println("Order confirmed, proceeding with payment: " + orderEvent.getOrderId());
            paymentService.processPayment(orderEvent.getOrderId());
        }
    }
}
