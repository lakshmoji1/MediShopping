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

    @PostMapping("/addItem/batch/{batchId}/quantity/{num}/user/{userId}")
    public CartItem addOrderItem(@PathVariable Integer userId, @PathVariable Integer batchId, @PathVariable Integer num) {
        User user = userRepository.findById(userId).get();
        Batch selectedBatch = batchRepository.findById(batchId).get();
        Integer currentQuantity = selectedBatch.getQuantity();
        List<Cart> customerCart = cartRepository.findAllByUserId(userId);
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
                        selectedBatch.setQuantity(currentQuantity-num + updateItem.getQuantity());
                        updateItem.setTotal(selectedBatch.getMrp()*num);
                        updateItem.setQuantity(num);
                    }
                }
                cart.setTotalAmount(cart.getTotalAmount()+ item.getTotal()-amountToBeReduced);
            }
            else {
                cart.addCartItem(item);
                cart.setTotalAmount(cart.getTotalAmount()+item.getTotal());
                selectedBatch.setQuantity(currentQuantity-item.getQuantity());
            }
            cart.setUser(user);
            cartRepository.save(cart);
            batchRepository.save(selectedBatch);
            return item;
        } else {
            Cart newCustomerCart = cartRepository.save(new Cart(0L, Status.NotPlaced, userId));
            CartItem item = new CartItem(num, selectedBatch.getMrp()*num, selectedBatch);
            item.setCart(newCustomerCart);
            newCustomerCart.addCartItem(item);
            newCustomerCart.setTotalAmount(newCustomerCart.getTotalAmount()+ item.getTotal());
            newCustomerCart.setUser(user);
            selectedBatch.setQuantity(currentQuantity - item.getQuantity());
            cartRepository.save(newCustomerCart);
            return item;
        }
    }
}
