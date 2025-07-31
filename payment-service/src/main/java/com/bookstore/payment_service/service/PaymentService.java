package com.bookstore.payment_service.service;

import com.bookstore.payment_service.entity.UserBalance;

public interface PaymentService {

    UserBalance addUser(UserBalance userBalance);

    void processPayment(Long orderId, Long id, Double amount);

    void refundPayment(Long orderId, Long id, Double amount);
}
