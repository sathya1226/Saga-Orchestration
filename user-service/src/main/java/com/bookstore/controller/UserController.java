package com.bookstore.controller;

import com.bookstore.dto.UserRequestDTO;
import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequestDTO dto){
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User Deleted Successfully");
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> checkBalance(@PathVariable Long id){
        return ResponseEntity.ok(userService.checkBalance(id));
    }

    @PostMapping("/{id}/deduct")
    public ResponseEntity<String> deductBalance(@PathVariable Long id, @RequestParam Double amount){
        return ResponseEntity.ok(userService.deductBalance(id, amount));
    }
}
