package com.example.webapp.controller;

import com.example.webapp.entity.Cart;
import com.example.webapp.entity.Status;
import com.example.webapp.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/customer/{id}/{orderStatus}")
    public List<Cart> getCartByStatus(@PathVariable String orderStatus, @PathVariable Integer id) {
        List<Cart> cartList = cartRepository.findAllByCustomerId(id);
        if(orderStatus.equals("NotPlaced")) {
            return cartList.stream().filter(cart -> cart.getStatus().equals(Status.NotPlaced)).collect(Collectors.toList());
        } else {
            return cartList.stream().filter(cart -> cart.getStatus().equals(Status.Placed)).collect(Collectors.toList());
        }
    }

}
