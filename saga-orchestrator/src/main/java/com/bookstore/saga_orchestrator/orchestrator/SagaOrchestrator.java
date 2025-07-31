package com.bookstore.saga_orchestrator.orchestrator;


import com.bookstore.common.enums.OrderStatus;
import com.bookstore.common.event.BookEvent;
import com.bookstore.common.event.OrderEvent;
import com.bookstore.common.event.PaymentEvent;
import com.bookstore.delivery_service.event.DeliveryEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SagaOrchestrator {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-topic", groupId = "orchestrator-group")
    public void handleOrderEvent(OrderEvent orderEvent) {
        if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            BookEvent bookEvent = new BookEvent(orderEvent.getOrderId(), Math.toIntExact(orderEvent.getBookId()), OrderStatus.BOOK_RESERVATION_REQUESTED, 1L);
            kafkaTemplate.send("book-topic", bookEvent);
        } else if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_FAILED)) {
            System.out.println("Order failed, initiating rollback or notification.");
        }
    }

    @KafkaListener(topics = "book-topic", groupId = "orchestrator-group")
    public void handleBookEvent(BookEvent bookEvent) {
        if (bookEvent.getOrderStatus().equals(OrderStatus.BOOK_RESERVED)) {
            PaymentEvent paymentEvent = new PaymentEvent(bookEvent.getOrderId(), 500.0, OrderStatus.PAYMENT_REQUESTED );
            kafkaTemplate.send("payment-topic", paymentEvent);
        } else if (bookEvent.getOrderStatus().equals(OrderStatus.BOOK_UNAVAILABLE)) {
            OrderEvent orderFailedEvent = new OrderEvent(bookEvent.getOrderId(), null, null, 0, OrderStatus.ORDER_FAILED);
            kafkaTemplate.send("order-topic", orderFailedEvent);
        }
    }

    @KafkaListener(topics = "payment-topic", groupId = "orchestrator-group")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        if (paymentEvent.getOrderStatus().equals(OrderStatus.PAYMENT_COMPLETED)) {
            DeliveryEvent deliveryEvent = new DeliveryEvent(paymentEvent.getOrderId(), "Default Address", "DELIVERY_INITIATED");
            kafkaTemplate.send("delivery-topic", deliveryEvent);
        } else if (paymentEvent.getOrderStatus().equals(OrderStatus.PAYMENT_FAILED)) {
            // Mark order failed
            OrderEvent orderFailedEvent = new OrderEvent(paymentEvent.getOrderId(), null, null, 0, OrderStatus.ORDER_FAILED);
            kafkaTemplate.send("order-topic", orderFailedEvent);
        }
    }

    @KafkaListener(topics = "delivery-topic", groupId = "orchestrator-group")
    public void handleDeliveryEvent(DeliveryEvent deliveryEvent) {
        if ("DELIVERY_COMPLETED".equals(deliveryEvent.getStatus())) {
            OrderEvent orderCompletedEvent = new OrderEvent(deliveryEvent.getOrderId(), null, null, 0, OrderStatus.ORDER_COMPLETED);
            kafkaTemplate.send("order-topic", orderCompletedEvent);
        }
    }
}
