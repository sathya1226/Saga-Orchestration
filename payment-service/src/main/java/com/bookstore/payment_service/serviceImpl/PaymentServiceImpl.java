package com.bookstore.payment_service.serviceImpl;

import com.bookstore.payment_service.entity.Payment;
import com.bookstore.payment_service.enums.PaymentStatus;
import com.bookstore.payment_service.event.PaymentEvent;
import com.bookstore.payment_service.kafka.Producer.PaymentEventProducer;
import com.bookstore.payment_service.respository.PaymentRepository;
import com.bookstore.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer producer;

    @Override
    public void processPayment(Long orderId){
        boolean success = new Random().nextBoolean();
        PaymentStatus status = success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(500.0)
                .status(status)
                .build();
        paymentRepository.save(payment);

        PaymentEvent event = PaymentEvent.builder()
                .orderId(orderId)
                .paymentStatus(status.name())
                .eventType(status == PaymentStatus.SUCCESS ? "PAYMENT_COMPLETED" : "PAYMENT FAILED")
                .build();
        producer.sendPaymentEvent(event);
    }
}
