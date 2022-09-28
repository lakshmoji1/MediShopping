package com.example.webapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer batchId;
    private String number;
    private Long buy;
    private Long free;
    private Integer quantity = 100;
    private Long mrp;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "medicine_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Medicine medicine;

    public Batch(String number, Long buy, Long free, Integer quantity, Long mrp, Medicine medicine) {
        this.number = number;
        this.buy = buy;
        this.free = free;
        this.quantity = quantity;
        this.mrp = mrp;
        this.medicine = medicine;
    }

    public Integer decreaseQuantity(Integer amount) {
        quantity -= amount;
        return quantity;
    }
}
