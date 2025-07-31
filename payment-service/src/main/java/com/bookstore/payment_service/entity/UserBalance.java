package com.bookstore.payment_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usersbalance")
@Data
public class UserBalance {

    @Id

    private Long id;

    private Double balance;

    private String name;
}
