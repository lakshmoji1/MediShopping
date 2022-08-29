package com.example.webapp.controller;

import com.example.webapp.entity.Credentials;
import com.example.webapp.entity.Role;
import com.example.webapp.repository.CredentialsRepository;
import com.example.webapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/credentials")
public class CredentialsController {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/save")
    public Credentials createCredentials(@RequestBody Credentials credentials) {
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleRepository.findById(1).get());
        credentials.addRole(userRoles);
        return credentialsRepository.save(credentials);
    }
}
