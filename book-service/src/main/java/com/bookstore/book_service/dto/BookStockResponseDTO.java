package com.bookstore.book_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookStockResponseDTO {
    private Long bookId;
    private String title;
    private Integer stock;
}
