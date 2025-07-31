package com.bookstore.payment_service.respository;

import com.bookstore.payment_service.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<UserBalance, Long> {
    Optional<UserBalance> findById(Long id);
}
