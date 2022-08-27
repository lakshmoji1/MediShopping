package com.example.webapp.controller;

import com.example.webapp.entity.Cart;
import com.example.webapp.entity.Status;
import com.example.webapp.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/user/{userId}/{orderStatus}")
    public List<Cart> getCartByStatus(@PathVariable String orderStatus, @PathVariable Integer userId) {
        List<Cart> cartList = cartRepository.findAllByUserId(userId);
        if(orderStatus.equals("NotPlaced")) {
            return cartList.stream().filter(cart -> cart.getStatus().equals(Status.NotPlaced)).collect(Collectors.toList());
        } else {
            return cartList.stream().filter(cart -> cart.getStatus().equals(Status.Placed)).collect(Collectors.toList());
        }
    }

    @PutMapping("/user/{userId}/placeOrder/{cartId}")
    public Cart placeCustomerCart(@PathVariable Integer userId, @PathVariable Integer cartId) {
        List<Cart> userCartList = cartRepository.findAllByUserId(userId);
        Cart userCart = userCartList.stream().filter(cart -> cart.getStatus().equals(Status.NotPlaced)).collect(Collectors.toList()).get(0);
        userCart.setStatus(Status.Placed);
        return cartRepository.save(userCart);
    }

}
