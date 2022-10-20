package com.example.webapp.controller;

import com.example.webapp.entity.Batch;
import com.example.webapp.entity.Medicine;
import com.example.webapp.exception.ResourceNotFoundException;
import com.example.webapp.repository.BatchRepository;
import com.example.webapp.repository.MedicineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Medicine available with id "+id));;
        batch.setMedicine(medicine);
        return batchRepository.save(batch);
    }

    @PutMapping("/{id}")
    public Batch update(@RequestBody Batch batch, @PathVariable Integer id) {
        Optional<Batch> oldBatch = batchRepository.findById(id);
        Medicine defaultMedicine = medicineRepository.findById(1).get();
        if(oldBatch.isPresent()) {
            oldBatch.get().setBuy(batch.getBuy());
            oldBatch.get().setFree(batch.getFree());
            oldBatch.get().setMrp(batch.getMrp());
            oldBatch.get().setMrp(batch.getMrp());
            return batchRepository.save(oldBatch.get());
        } else {
            batch.setMedicine(defaultMedicine);
            return batchRepository.save(batch);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        Batch optionalBatch = batchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Batch available with id "+id));;
        batchRepository.delete(optionalBatch);
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getAll() {
        return ResponseEntity.ok(batchRepository.findAll());
    }

    @GetMapping("/{id}")
    public Batch getById(@PathVariable Integer id) {
        Batch optionalBatch = batchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Batch available with id "+id));;
        if (optionalBatch != null) {
            return optionalBatch;
        }
        return null;
    }

    @GetMapping("/{id}/medicine")
    public String getMedicineNameByBatchId(@PathVariable Integer id) {
        Batch optionalBatch = batchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Batch available with id "+id));;
        if (optionalBatch != null) {
            return optionalBatch.getMedicine().getName();
        } else
            return null;
    }

    @GetMapping("/fake/api")
    public List<Movie> getHello() throws JsonProcessingException {
        String uri = "https://mocki.io/v1/d4867d8b-b5d5-4a48-a4ab-79131b5809b8";
        RestTemplate restTemplate = new RestTemplate();
        String movies = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Movie[] movieList = mapper.readValue(movies, Movie[].class);
        ArrayList<Movie> list = new ArrayList<Movie>();
        for(Movie mov:movieList) {
            list.add(mov);
            System.out.println(mov.city);
            System.out.println(mov.name);
        }
        return list.stream().filter(movie -> !movie.city.equals("Alabama")).collect(Collectors.toList());
    }
}

class Movie {
    public String name;
    public String city;
}


