package com.bookstore.payment_service.kafka.Consumer;

import com.bookstore.payment_service.event.PaymentEvent;
import com.bookstore.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentRollbackListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "payment-rollback-topic", groupId = "payment-service-group")
    public void listenRollbackEvent(PaymentEvent event) {
        System.out.println("Received refund event: " + event);
        if ("FAILED".equals(event.getStatus())) {
            paymentService.refundPayment(event.getOrderId(), event.getId(), event.getAmount());
        }
    }
}
