package com.example.webapp.repository;

import com.example.webapp.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    Medicine findByName(String name);
}
