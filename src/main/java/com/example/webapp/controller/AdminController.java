package com.example.webapp.controller;

import com.example.webapp.entity.*;
import com.example.webapp.repository.BatchRepository;
import com.example.webapp.repository.CartRepository;
import com.example.webapp.repository.PaymentHistoryRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @PutMapping("/order/{cartId}")
    public void updateOrder(@PathVariable Integer cartId,  @RequestParam(name = "updateToStatus", defaultValue = "Placed") String updateToStatus) {
        Cart userCart = cartRepository.findById(cartId).get();
        Integer customerId = userCart.getCustomerId();
        User currentUser = userRepository.findById(customerId).get();
        if(currentUser.getShop().getBalance() < 5000) {
            if (userCart.getStatus().equals(Status.Waiting)) {
                if (updateToStatus.equals("Placed")) {
                    userCart.setStatus(Status.Placed);
                    currentUser.getShop().addOrderAmountToBalance(userCart.getTotalAmount());
                    PaymentHistory paymentHistory = new PaymentHistory(PaymentType.Received, new Date(), userCart.getTotalAmount(), currentUser);
                    paymentHistoryRepository.save(paymentHistory);
                    List<Batch> updateAllBatches = new ArrayList<>();
                    for (CartItem cartItem : userCart.getCartItems()) {
                        Batch updateBatch = batchRepository.findById(cartItem.getBatch().getBatchId()).get();
                        updateBatch.decreaseQuantity(cartItem.getQuantity());
                        updateAllBatches.add(updateBatch);
                        System.out.println("Updating batch quantity to "+updateBatch.getQuantity().toString());
                        batchRepository.saveAll(updateAllBatches);
                    }
                } else {
                    userCart.setStatus(Status.Cancelled);
                    cartRepository.save(userCart);
                    System.out.println("Admin has cancelled the order");
                }
            } else {
                System.out.println("Cannot place the order since user has not placed it yet");
            }
        } else {
            userCart.setStatus(Status.Cancelled);
            cartRepository.save(userCart);
            System.out.println("Your order has been rejected since total due amount is greater than 5000");
        }
    }
}
