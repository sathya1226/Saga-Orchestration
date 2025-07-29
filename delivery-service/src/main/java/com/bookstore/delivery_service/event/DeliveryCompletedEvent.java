package com.bookstore.delivery_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompletedEvent {

    private Long orderId;
    private String status;
    private String address;
}
