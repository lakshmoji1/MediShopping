package com.example.webapp.controller;

import com.example.webapp.entity.Batch;
import com.example.webapp.entity.Medicine;
import com.example.webapp.repository.BatchRepository;
import com.example.webapp.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {
    private final BatchRepository batchRepository;
    private final MedicineRepository medicineRepository;

    @Autowired
    public BatchController(BatchRepository batchRepository, MedicineRepository medicineRepository) {
        this.batchRepository = batchRepository;
        this.medicineRepository = medicineRepository;
    }

    @PostMapping("/medicine/{id}")
    public Batch create(@PathVariable Integer id, @RequestBody Batch batch) {
        Medicine medicine = medicineRepository.findById(id).get();
        batch.setMedicine(medicine);
        return batchRepository.save(batch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> update(@RequestBody Batch batch, @PathVariable Integer id) {
        Optional<Batch> oldBatch = batchRepository.findById(id);
        Medicine defaultMedicine = medicineRepository.findById(1).get();
        if(oldBatch == null) {
            batch.setMedicine(defaultMedicine);
        } else {
            batch.setBatchId(oldBatch.get().getBatchId());
            batch.setMedicine(oldBatch.get().getMedicine());
        }
        batchRepository.save(batch);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Batch> delete(@PathVariable Integer id) {
        Optional<Batch> optionalBatch = batchRepository.findById(id);
        if (!optionalBatch.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        batchRepository.delete(optionalBatch.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getAll() {
        return ResponseEntity.ok(batchRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getById(@PathVariable Integer id) {
        Optional<Batch> optionalBatch = batchRepository.findById(id);
        if (!optionalBatch.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalBatch.get());
    }

    @GetMapping("/{id}/medicine")
    public String getMedicineNameByBatchId(@PathVariable Integer id) {
        Optional<Batch> optionalBatch = batchRepository.findById(id);
        if (optionalBatch.isPresent()) {
            return optionalBatch.get().getMedicine().getName();
        } else
            return null;
    }

}


