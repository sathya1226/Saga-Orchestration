package com.bookstore.delivery_service.controller;

import com.bookstore.delivery_service.dto.DeliveryRequestDTO;
import com.bookstore.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<String> deliver(@RequestBody DeliveryRequestDTO dto){
        deliveryService.processDelivery(dto);
        return ResponseEntity.ok("Delivery processed");
    }

}
