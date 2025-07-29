package com.bookstore.payment_service.kafka.Producer;

import com.bookstore.payment_service.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent event){
        kafkaTemplate.send("payment-status", event);
    }
}
