package com.bookstore.common.event;

import com.bookstore.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {

    private Long orderId;
    private Double amount;
    private OrderStatus orderStatus;
}
