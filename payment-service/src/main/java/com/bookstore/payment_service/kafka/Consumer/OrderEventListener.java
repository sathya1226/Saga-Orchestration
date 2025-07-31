package com.bookstore.payment_service.kafka.Consumer;

import com.bookstore.payment_service.event.OrderEvent;
import com.bookstore.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order-topic", groupId = "payment-service-group")
    public void consumeOrderEvent(OrderEvent event) {
        paymentService.processPayment(event.getOrderId(), event.getUserId(), event.getAmount());
    }
}
