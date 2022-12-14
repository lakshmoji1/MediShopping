package com.example.webapp.controller;

import com.example.webapp.entity.Batch;
import com.example.webapp.entity.Company;
import com.example.webapp.entity.Medicine;
import com.example.webapp.model.MedicineWithBatch;
import com.example.webapp.repository.CompanyRepository;
import com.example.webapp.repository.MedicineRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MediBatchController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping("/upload")
    @CacheEvict(value = "medicines", allEntries = true)
    public String index() {
        return "temp";
    }

    @PostMapping("/upload-csv-file")
    @ResponseBody
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            List<MedicineWithBatch> mediBatches = Collections.emptyList();
            List<Medicine> medicines = new ArrayList<>();
            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                // create csv bean reader
                CsvToBean<MedicineWithBatch> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(MedicineWithBatch.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users

                mediBatches = csvToBean.parse();

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
            System.out.println("Received "+mediBatches.size()+ " records");
            for(int i=0; i < mediBatches.size(); i++) {
                MedicineWithBatch currentMediBatch = mediBatches.get(i);
                System.out.println(currentMediBatch);
                Medicine medicine = medicineRepository.findByName(currentMediBatch.medicineName);
                if(medicine == null){
                    Company company = companyRepository.findByName(currentMediBatch.companyName);
                    medicine = new Medicine(currentMediBatch.medicineName, company);
                }
                Batch newBatch = new Batch(currentMediBatch.batchNumber, currentMediBatch.getBuy(), currentMediBatch.getFree(), currentMediBatch.getQuantity() , currentMediBatch.getPrice(), medicine);
                medicine.getBatches().add(newBatch);
                medicines.add(medicine);

            }
            medicineRepository.saveAll(medicines);
        }
        return "Successfully uploaded the medicines into database";
    }
}
