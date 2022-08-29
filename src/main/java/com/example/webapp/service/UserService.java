package com.example.webapp.service;

import com.example.webapp.entity.User;
import com.example.webapp.exception.ResourceNotFoundException;
import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: "+id.toString()+" isn't available"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(@RequestBody User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist, cannot update the details"));
        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());
        return userRepository.save(existingUser);
    }
}
