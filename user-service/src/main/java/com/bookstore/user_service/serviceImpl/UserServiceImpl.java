package com.bookstore.user_service.serviceImpl;

import com.bookstore.user_service.dto.UserCreateRequestDTO;
import com.bookstore.user_service.entity.User;
import com.bookstore.user_service.repository.UserRepository;
import com.bookstore.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserCreateRequestDTO userRequestDTO){
        User user = User.builder()
                .name(userRequestDTO.getName())
                .balance(userRequestDTO.getAmount())
                .build();
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public Double checkBalance(Long userId){
        User user = getUserById(userId);
        return user.getBalance();
    }

    @Override
    public String deductBalance(Long userId, Double amount){
        User user = getUserById(userId);
        if(user.getBalance() >= amount){
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
            return "Balance deducted successfully";
        }else {
            return "Insufficient balance";
        }
    }
}
