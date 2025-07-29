package com.bookstore.delivery_service.serviceImpl;

import com.bookstore.delivery_service.dto.DeliveryRequestDTO;
import com.bookstore.delivery_service.entity.Delivery;
import com.bookstore.delivery_service.event.DeliveryCompletedEvent;
import com.bookstore.delivery_service.event.PaymentEvent;
import com.bookstore.delivery_service.repository.DeliveryRepository;
import com.bookstore.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final KafkaTemplate<String, DeliveryCompletedEvent> kafkaTemplate;

    @KafkaListener(topics = "payment.completed", groupId = "delivery-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenToPaymentEvent(PaymentEvent paymentEvent) {
        log.info("Received PaymentEvent: {}", paymentEvent);

        if ("COMPLETED".equalsIgnoreCase(paymentEvent.getStatus())) {
            Optional<Delivery> existing = deliveryRepository.findByOrderId(paymentEvent.getOrderId());

            if (existing.isEmpty()) {
                Delivery delivery = new Delivery();
                delivery.setOrderId(paymentEvent.getOrderId());
                delivery.setAddress(paymentEvent.getAddress());
                delivery.setStatus("DELIVERED");

                deliveryRepository.save(delivery);


                DeliveryCompletedEvent deliveryCompletedEvent = new DeliveryCompletedEvent(
                        paymentEvent.getOrderId(), "DELIVERED", paymentEvent.getAddress()
                );

                kafkaTemplate.send("delivery.completed", deliveryCompletedEvent);
                log.info("Published DeliveryCompletedEvent to Kafka: {}", deliveryCompletedEvent);
            } else {
                log.warn("Delivery already processed for orderId: {}", paymentEvent.getOrderId());
            }
        } else {
            log.warn("Payment status not COMPLETED. Ignoring Delivery.");
        }
    }


    public void processDelivery(DeliveryRequestDTO dto) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(dto.getOrderId());
        delivery.setAddress(dto.getAddress());
        delivery.setStatus("DELIVERED");
        deliveryRepository.save(delivery);

        DeliveryCompletedEvent event = new DeliveryCompletedEvent(dto.getOrderId(), "DELIVERED", dto.getAddress());
        kafkaTemplate.send("delivery.completed", event);
    }
}
