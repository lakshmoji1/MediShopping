package com.example.webapp.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Cart> cartList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private Shop shop;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Credentials credentials;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        for(Cart cart : cartList) {
            cart.setUser(this);
        }
        this.cartList = cartList;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        shop.setOwner(this);
        this.shop = shop;
    }

    public void setCredentials(Credentials credentials) {
        credentials.setUser(this);
//        List<Role> userRoles = new ArrayList<Role>();
//        userRoles.add(new Role(1, "Admin", "manage everything"));
//        userRoles.add(new Role(2, "Assistant", "manage questions and reviews"));
//        credentials.addRole(userRoles);
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

}
