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
        System.out.println("Saga Received OrderEvent: " + orderEvent);
        if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            BookEvent bookEvent = new BookEvent(orderEvent.getOrderId(),
                    orderEvent.getBookId(),
                    OrderStatus.BOOK_RESERVATION_REQUESTED,
                    (long) orderEvent.getQuantity());
            kafkaTemplate.send("book-topic", String.valueOf(orderEvent.getOrderId()), bookEvent);
        } else if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_FAILED)) {
            System.out.println("Order failed at initial stage.");
        }
    }

    @KafkaListener(topics = "book-topic", groupId = "orchestrator-group")
    public void handleBookEvent(BookEvent bookEvent) {
        System.out.println("Saga Received BookEvent: " + bookEvent);
        if (bookEvent.getOrderStatus().equals(OrderStatus.BOOK_RESERVED)) {
            PaymentEvent paymentEvent = new PaymentEvent(bookEvent.getOrderId(),
                    500.0, // You can replace this with dynamic pricing
                    OrderStatus.PAYMENT_REQUESTED);
            kafkaTemplate.send("payment-topic", String.valueOf(bookEvent.getOrderId()), paymentEvent);
        } else if (bookEvent.getOrderStatus().equals(OrderStatus.BOOK_UNAVAILABLE)) {
            OrderEvent failedEvent = new OrderEvent(bookEvent.getOrderId(),
                    null, null, 0.0, null,
                    OrderStatus.ORDER_FAILED, 0);
            kafkaTemplate.send("order-topic", String.valueOf(bookEvent.getOrderId()), failedEvent);
        }
    }

    @KafkaListener(topics = "payment-topic", groupId = "orchestrator-group")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        System.out.println("Saga Received PaymentEvent: " + paymentEvent);
        if (paymentEvent.getOrderStatus().equals(OrderStatus.PAYMENT_COMPLETED)) {
            DeliveryEvent deliveryEvent = new DeliveryEvent(paymentEvent.getOrderId(),
                    "Default Address",
                    "DELIVERY_INITIATED");
            kafkaTemplate.send("delivery-topic", String.valueOf(paymentEvent.getOrderId()), deliveryEvent);
        } else if (paymentEvent.getOrderStatus().equals(OrderStatus.PAYMENT_FAILED)) {
            kafkaTemplate.send("payment-rollback-topic", paymentEvent);
            OrderEvent failedEvent = new OrderEvent(paymentEvent.getOrderId(),
                    null, null, 0.0, null,
                    OrderStatus.ORDER_FAILED, 0);

            kafkaTemplate.send("order-topic", String.valueOf(paymentEvent.getOrderId()), failedEvent);
        }
    }

    @KafkaListener(topics = "delivery-topic", groupId = "orchestrator-group")
    public void handleDeliveryEvent(DeliveryEvent deliveryEvent) {
        System.out.println("Saga Received DeliveryEvent: " + deliveryEvent);
        if ("DELIVERY_COMPLETED".equals(deliveryEvent.getStatus())) {
            OrderEvent completeEvent = new OrderEvent(
                    deliveryEvent.getOrderId(),
                    null, null, 0.0, null,
                    OrderStatus.ORDER_COMPLETED, 0);

            kafkaTemplate.send("order-topic", String.valueOf(deliveryEvent.getOrderId()), completeEvent);
        }
    }
}
