package com.bookstore.delivery_service.service;

import com.bookstore.delivery_service.dto.DeliveryRequestDTO;

public interface DeliveryService {

    void processDelivery(DeliveryRequestDTO dto);
}
