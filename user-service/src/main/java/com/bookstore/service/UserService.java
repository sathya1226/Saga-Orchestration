package com.bookstore.service;


import com.bookstore.dto.UserRequestDTO;
import com.bookstore.entity.User;
import java.util.List;

public interface UserService {

    User createUser(UserRequestDTO userRequestDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    Double checkBalance(Long userId);
    String deductBalance(Long userId, Double amount);
}
