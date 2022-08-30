package com.example.webapp.service;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import com.example.webapp.exception.ResourceNotFoundException;
import com.example.webapp.model.UserModel;
import com.example.webapp.repository.RoleRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: "+id.toString()+" isn't available"));
    }

    public User save(UserModel userModel) {
        User user = userModel.getUser();
        Set<Role> userRoles = new HashSet<Role>();
        for(String roleType : userModel.getUserRoles()) {
            userRoles.add(roleRepository.findByName(roleType));
        }
        user.getCredentials().setRoles(userRoles);
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
