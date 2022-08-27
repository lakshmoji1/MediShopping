package com.example.webapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(exclude = {"cart"})
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;
    private Integer quantity;
    private Long total;

    @OneToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cart_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cart cart;

    public CartItem(Integer quantity, Long total, Batch batch, Cart cart) {
        this.quantity = quantity;
        this.total = total;
        this.batch = batch;
        this.cart = cart;
    }

    public CartItem(Integer quantity, Long total, Batch batch) {
        this.quantity = quantity;
        this.total = total;
        this.batch = batch;
    }
}
