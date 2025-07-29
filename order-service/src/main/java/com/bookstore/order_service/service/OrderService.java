package com.bookstore.order_service.service;

import com.bookstore.order_service.dto.OrderRequestDTO;
import com.bookstore.order_service.entity.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(OrderRequestDTO dto);

    void updateOrderStatus(Long orderId, String eventType);

    List<Order> getAllOrders();
}
