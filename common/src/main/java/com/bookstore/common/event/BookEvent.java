package com.bookstore.common.event;


import com.bookstore.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEvent {

    private Long orderId;
    private Long bookId;
    private OrderStatus orderStatus;
    private Long userId;
}
