package com.example.webapp.controller;

import com.example.webapp.entity.Batch;
import com.example.webapp.entity.Company;
import com.example.webapp.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.webapp.entity.Medicine;
import com.example.webapp.repository.BatchRepository;
import com.example.webapp.repository.MedicineRepository;

@RestController
@RequestMapping("/api/v1/medicines")
public class MedicineController {
    private final MedicineRepository medicineRepository;
    private final BatchRepository batchRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public MedicineController(MedicineRepository medicineRepository, BatchRepository batchRepository, CompanyRepository companyRepository) {
        this.medicineRepository = medicineRepository;
        this.batchRepository = batchRepository;
        this.companyRepository = companyRepository;
    }

    @PostMapping("/company/{id}")
    @CacheEvict(value = "medicines", allEntries = true)
    public Medicine create(@PathVariable Integer id, @RequestBody Medicine medicine) {
        Company company = companyRepository.findById(id).get();
        medicine.setCompany(company);
        Medicine savedMedicine = medicineRepository.save(medicine);
        return savedMedicine;
    }

    @PutMapping("/{id}")
    @Caching(evict = {
            @CacheEvict(value = "medicines", allEntries = true),
            @CacheEvict(value = "medicine", key = "#p0")
    })
    public ResponseEntity<Medicine> update(@PathVariable Integer id,@RequestBody Medicine medicine) {
       Medicine oldMedicine = medicineRepository.findById(id).get();
        medicine.setMedicineId(oldMedicine.getMedicineId());
        medicine.setCompany(oldMedicine.getCompany());
        medicineRepository.save(medicine);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Caching(evict = {
            @CacheEvict(value = "medicines", allEntries = true),
            @CacheEvict(value = "medicine", key = "#p0")
    })
    public ResponseEntity<Medicine> delete(@PathVariable Integer id) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (!optionalMedicine.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        medicineRepository.delete(optionalMedicine.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Cacheable(value = "medicine", key = "#p0")
    public ResponseEntity<Medicine> getById(@PathVariable Integer id) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (!optionalMedicine.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalMedicine.get());
    }

    @GetMapping
    public ResponseEntity<List<Medicine>> getAll() {
        return ResponseEntity.ok(medicineRepository.findAll());
    }

    @GetMapping("/quantities")
    public List<MedicineQuantity> getQuantity() {
        List<MedicineQuantity> medicineQuantities = new ArrayList<>();
        for(Medicine medicine : medicineRepository.findAll()) {
            Integer sum = 0;
            List<BatchQuantity> batchQuantities = new ArrayList<>();
            for(Batch batch: medicine.getBatches()) {
                batchQuantities.add(new BatchQuantity(batch.getNumber(), batch.getQuantity()));
                sum += batch.getQuantity();
            }
            medicineQuantities.add(new MedicineQuantity(medicine.getName(), batchQuantities, sum));
        }
        return medicineQuantities;
    }

    @GetMapping("/all")
    @Cacheable("medicines")
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MedicineQuantity {
    String name;
    List<BatchQuantity> batchQuantities;
    Integer total;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class BatchQuantity {
    String number;
    Integer quantity;
}

