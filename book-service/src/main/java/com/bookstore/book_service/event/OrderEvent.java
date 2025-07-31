package com.bookstore.book_service.event;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long bookId;
    private Long userId;
    private int quantity;
}
