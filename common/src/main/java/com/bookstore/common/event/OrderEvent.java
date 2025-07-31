package com.bookstore.common.event;

import com.bookstore.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {

    private Long orderId;
    private Long bookId;
    private Long userId;
    private Double amount;
    private String eventType;
    private OrderStatus orderStatus;
    private Integer quantity;
}
