package com.bookstore.user_service.service;


import com.bookstore.user_service.dto.UserCreateRequestDTO;
import com.bookstore.user_service.entity.User;
import java.util.List;

public interface UserService {

    User createUser(UserCreateRequestDTO userRequestDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    Double checkBalance(Long userId);
    String deductBalance(Long userId, Double amount);
}
