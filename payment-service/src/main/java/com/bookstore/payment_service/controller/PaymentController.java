package com.bookstore.payment_service.controller;

import com.bookstore.payment_service.entity.UserBalance;
import com.bookstore.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/add-user")
    public UserBalance addUser(@RequestBody UserBalance userBalance){
        return paymentService.addUser(userBalance);
    }

    @PostMapping("/process")
    public String processPayment(
            @RequestParam Long orderId,
            @RequestParam Long userId,
            @RequestParam Double amount
    ) {
        paymentService.processPayment(orderId, userId, amount);
        return "Payment processing started for Order ID: " + orderId;
    }

    @PostMapping("/refund")
    public String refundPayment(
            @RequestParam Long orderId,
            @RequestParam Long userId,
            @RequestParam Double amount
    ) {
        paymentService.refundPayment(orderId, userId, amount);
        return "Refund initiated for Order ID: " + orderId;
    }
}
