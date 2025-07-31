package com.bookstore.order_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookStockEvent {

    private Long orderId;
    private Long bookId;
    private boolean stockAvailable;
    private String eventType;
}
