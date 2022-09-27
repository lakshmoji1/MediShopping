package com.example.webapp.controller;

import com.example.webapp.entity.Company;
import com.example.webapp.repository.CompanyRepository;
import com.example.webapp.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @PostMapping
    public Company create(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    @GetMapping
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Company findCompanyById(@PathVariable Integer id) {
        return companyRepository.findById(id).get();
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable Integer id, @RequestBody Company company) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if(optionalCompany.isPresent()) {
            company.setCompanyId(optionalCompany.get().getCompanyId());
            return companyRepository.save(company);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteCompanyById(@PathVariable Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if(optionalCompany.isPresent()) {
            companyRepository.deleteById(id);
            return "Company "+optionalCompany.get().getName()+" is deleted";
        }
        return "Company with id "+id+" isn't available";
    }
}
