package com.bookstore.payment_service.serviceImpl;

import com.bookstore.payment_service.entity.UserBalance;
import com.bookstore.payment_service.enums.PaymentStatus;
import com.bookstore.payment_service.event.PaymentEvent;
import com.bookstore.payment_service.kafka.Producer.PaymentEventProducer;
import com.bookstore.payment_service.respository.PaymentRepository;
import com.bookstore.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer producer;

    @Override
    public UserBalance addUser(UserBalance userBalance) {
        return paymentRepository.save(userBalance);
    }

    @Override
    public void processPayment(Long orderId, Long id, Double amount) {
        Optional<UserBalance> userOpt = paymentRepository.findById(id);
        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(orderId);
        paymentEvent.setId(id);
        paymentEvent.setAmount(amount);


        if (userOpt.isPresent()) {
            UserBalance userBalance = userOpt.get();
            if (userBalance.getBalance() >= amount) {
                // Deduct amount
                userBalance.setBalance(userBalance.getBalance() - amount);
                paymentRepository.save(userBalance);
                paymentEvent.setStatus("SUCCESS");
                paymentEvent.setMessage("Payment successful");
            } else {
                paymentEvent.setStatus("FAILED");
                paymentEvent.setMessage("Insufficient Balance");
            }
        } else {
            paymentEvent.setStatus("FAILED");
            paymentEvent.setMessage("User not found");
        }

        producer.sendPaymentEvent(paymentEvent);
    }

    @Override
    public void refundPayment(Long orderId, Long id, Double amount) {
        Optional<UserBalance> userOpt = paymentRepository.findById(id);

        if (userOpt.isPresent()) {
            UserBalance userBalance = userOpt.get();
            userBalance.setBalance(userBalance.getBalance() + amount);
            paymentRepository.save(userBalance);
            System.out.println("Refunded amount " + amount + " to userId: " + id);
        } else {
            System.out.println("Refund failed. User not found for userId: " + id);
        }
    }

}
