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
                .status(OrderStatus.PENDING)
                .build();

        order = orderRepository.save(order);

        OrderEvent orderEvent = OrderEvent.builder()
                .orderId(order.getId())
                .bookId(order.getBookId())
                .eventType("ORDER_CREATED")
                .build();

        orderEventProducer.sendOrderEvent(orderEvent);

        return order;
    }

    @Override
    public void updateOrderStatus(Long orderId, String eventType) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("BOOK_CONFIRMED".equals(eventType)) {
            order.setStatus(OrderStatus.CONFIRMED);
        } else {
            order.setStatus(OrderStatus.REJECTED);
        }

        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}
