package com.example.webapp.controller;

import com.example.webapp.entity.*;
import com.example.webapp.repository.CartRepository;
import com.example.webapp.repository.PaymentHistoryRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

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
        userCart.setStatus(Status.Waiting);
        return cartRepository.save(userCart);
    }

}
