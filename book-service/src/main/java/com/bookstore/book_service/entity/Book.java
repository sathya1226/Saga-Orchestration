package com.bookstore.book_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Double price;

        public void decreaseStock(int quantity){
            if (this.stock < quantity){
                throw new IllegalArgumentException("Insufficient stock");
            }
            this.stock -= quantity;
        }

}