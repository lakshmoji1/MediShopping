package com.example.webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    private Long amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public PaymentHistory(PaymentType type, Date transactionDate, Long amount, User user) {
        this.type = type;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.user = user;
    }
}