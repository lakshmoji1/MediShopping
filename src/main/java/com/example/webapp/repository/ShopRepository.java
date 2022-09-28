package com.example.webapp.repository;

import com.example.webapp.entity.Shop;
import com.example.webapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
}
