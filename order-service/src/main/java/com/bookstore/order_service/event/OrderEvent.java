package com.bookstore.order_service.event;

import com.bookstore.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent {

    private Long orderId;
    private Long userId;
    private Long bookId;
    private int quantity;
    private OrderStatus orderStatus;
    private String eventType;
}
