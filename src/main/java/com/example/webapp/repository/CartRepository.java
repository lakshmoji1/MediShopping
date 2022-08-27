package com.example.webapp.repository;

import com.example.webapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    boolean existsByCustomerId(Integer id);

    List<Cart> findAllByCustomerId(Integer id);
}
