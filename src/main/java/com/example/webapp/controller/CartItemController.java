package com.example.webapp.controller;

import com.example.webapp.entity.*;
import com.example.webapp.repository.BatchRepository;
import com.example.webapp.repository.CartItemRepository;
import com.example.webapp.repository.CartRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartItemController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addItem/batch/{batchId}/quantity/{num}/customer/{customerId}")
    public CartItem addOrderItem(@PathVariable Integer customerId, @PathVariable Integer batchId, @PathVariable Integer num) {
        User user = userRepository.findById(1).get();
        Batch selectedBatch = batchRepository.findById(batchId).get();
        List<Cart> customerCart = cartRepository.findAllByCustomerId(customerId);
        Cart requiredCart = null;
        for(Cart c:customerCart) {
            if(c.getStatus().equals(Status.NotPlaced))
                requiredCart = c;
        }
        if(requiredCart != null) {
            CartItem item = new CartItem(num, selectedBatch.getMrp()*num, selectedBatch, requiredCart);
            Cart cart = requiredCart;
            CartItem result = cart.isItemPresent(selectedBatch);
            if(result != null){
                Long amountToBeReduced = result.getTotal();
                for(CartItem updateItem : cart.getCartItems()) {
                    if(updateItem.equals(result)) {
                        updateItem.setTotal(selectedBatch.getMrp()*num);
                        updateItem.setQuantity(num);
                    }
                }
                cart.setTotalAmount(cart.getTotalAmount()+ item.getTotal()-amountToBeReduced);
            }
            else {
                cart.addCartItem(item);
                cart.setTotalAmount(cart.getTotalAmount()+item.getTotal());
            }
            cart.setUser(user);
            cartRepository.save(cart);
            return item;
        } else {
            Cart newCustomerCart = cartRepository.save(new Cart(0L, Status.NotPlaced, customerId));
            CartItem item = new CartItem(num, selectedBatch.getMrp()*num, selectedBatch);
            item.setCart(newCustomerCart);
            newCustomerCart.addCartItem(item);
            newCustomerCart.setTotalAmount(newCustomerCart.getTotalAmount()+ item.getTotal());
            newCustomerCart.setUser(user);
            cartRepository.save(newCustomerCart);
            return item;
        }
    }
}
