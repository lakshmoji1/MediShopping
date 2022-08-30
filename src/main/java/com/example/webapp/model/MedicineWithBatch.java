package com.example.webapp.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineWithBatch {
    @CsvBindByName
    public String medicineName;
    @CsvBindByName
    public String companyName;
    @CsvBindByName
    public String batchNumber;
    @CsvBindByName
    public Long buy;
    @CsvBindByName
    public Long free;
    @CsvBindByName
    public Integer quantity;
    @CsvBindByName
    public Long price;
}
