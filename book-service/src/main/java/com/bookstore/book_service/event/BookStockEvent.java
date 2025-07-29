package com.bookstore.book_service.event;

import com.bookstore.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookStockEvent {

    private Long orderId;
    private Long bookId;
    private OrderStatus orderStatus;
    private boolean isStockAvailable;
    private String eventType;
}
