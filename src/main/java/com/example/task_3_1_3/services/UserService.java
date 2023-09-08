package com.example.task_3_1_3.services;



import com.example.task_3_1_3.entities.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
    void deleteById(Long id);
    List<User> findAll();
    User findById(Long id);
}