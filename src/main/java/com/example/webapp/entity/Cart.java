package com.example.webapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private Long totalAmount;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NotPlaced;
    private Integer customerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Cart(Long totalAmount, Status status, Integer customerId) {
        this.totalAmount = totalAmount;
        this.status = status;
        this.customerId = customerId;
    }

    public Cart(Long totalAmount, Status status, Integer customerId, List<CartItem> cartItems) {
        this.totalAmount = totalAmount;
        this.status = status;
        this.customerId = customerId;
        this.cartItems = cartItems;
    }

    public void addCartItem(CartItem item) {
        this.cartItems.add(item);
    }

    public void removeCartItem(CartItem item) {
        this.cartItems.remove(item);
    }

    public CartItem isItemPresent(Batch batch) {
        for(CartItem item : this.cartItems) {
            if(item.getBatch().equals(batch)) {
                return item;
            }
        }
        return null;
    }

}
