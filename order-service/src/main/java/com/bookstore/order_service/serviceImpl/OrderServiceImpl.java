package com.bookstore.order_service.serviceImpl;

import com.bookstore.common.enums.OrderStatus;
import com.bookstore.order_service.dto.OrderRequestDTO;
import com.bookstore.order_service.entity.Order;
import com.bookstore.order_service.event.OrderEvent;
import com.bookstore.order_service.kafka.producer.OrderEventProducer;
import com.bookstore.order_service.respository.OrderRepository;
import com.bookstore.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Override
    public Order placeOrder(OrderRequestDTO request) {
        Order order = Order.builder()
                .bookId(request.getBookId())
                .userId(request.getUserId())
                .quantity(request.getQuantity())
                .status(OrderStatus.ORDER_CREATED)
                .build();


        order = orderRepository.save(order);

        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .bookId(order.getBookId())
                .userId(order.getUserId())
                .quantity(order.getQuantity())
                .orderStatus(order.getStatus())
                .eventType("ORDER_CREATED")
                .build();

        orderEventProducer.sendOrderEvent(event);
        return order;
    }

    @Override
    public void updateOrderStatus(Long orderId, String eventType) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        switch (eventType) {
            case "BOOK_CONFIRMED" -> order.setStatus(OrderStatus.BOOK_CONFIRMED);
            case "BOOK_REJECTED" -> order.setStatus(OrderStatus.BOOK_REJECTED);
            case "PAYMENT_COMPLETED" -> order.setStatus(OrderStatus.PAYMENT_COMPLETED);
            case "PAYMENT_FAILED" -> order.setStatus(OrderStatus.PAYMENT_FAILED);
            case "DELIVERED" -> order.setStatus(OrderStatus.DELIVERED);
            case "FAILED" -> order.setStatus(OrderStatus.FAILED);
            default -> throw new IllegalArgumentException("Unknown eventType: " + eventType);
        }

        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}
