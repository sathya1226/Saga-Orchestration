package com.bookstore.book_service.event;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookStockEvent {
    private Long bookId;
    private Long orderId;
    private boolean stockUpdated;
    private String message;
}
