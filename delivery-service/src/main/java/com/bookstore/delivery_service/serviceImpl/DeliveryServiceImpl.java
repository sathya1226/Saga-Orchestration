package com.bookstore.delivery_service.serviceImpl;

import com.bookstore.delivery_service.dto.DeliveryRequestDTO;
import com.bookstore.delivery_service.entity.Delivery;
import com.bookstore.delivery_service.event.DeliveryEvent;
import com.bookstore.delivery_service.repository.DeliveryRepository;
import com.bookstore.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final KafkaTemplate<String, DeliveryEvent> kafkaTemplate;

    @Override
    public void processDelivery(DeliveryRequestDTO dto){
        Delivery delivery = new Delivery();
        delivery.setOrderId(dto.getOrderId());
        delivery.setAddress(dto.getAddress());
        delivery.setStatus("DELIVERED");
        deliveryRepository.save(delivery);

        DeliveryEvent event = new DeliveryEvent(dto.getOrderId(), "DELIVERED", dto.getAddress());
        kafkaTemplate.send("delivery-topic", event);
    }
}
