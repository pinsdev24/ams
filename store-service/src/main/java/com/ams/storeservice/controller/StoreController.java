package com.ams.storeservice.controller;

import com.ams.storeservice.entity.Cart;
import com.ams.storeservice.entity.Toy;
import com.ams.storeservice.repository.CartRepository;
import com.ams.storeservice.repository.ToyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/toys")
public class StoreController {

    @Autowired
    private ToyRepository toyRepository;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Toy> getAllToys() {
        List<Toy> toys = toyRepository.findAll();
        return new ResponseEntity<>(toys, HttpStatus.OK).getBody();
    }

    @GetMapping("/carts/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        return optionalCart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/carts/{userId}") // Create cart if none exists
    public ResponseEntity<Cart> createCart(@PathVariable Long userId){
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if(optionalCart.isEmpty()){
            Cart cart = new Cart();
            cart.setUserId(userId);
            return new ResponseEntity<>(cartRepository.save(cart),HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(optionalCart.get(),HttpStatus.OK);
        }
    }

    @PostMapping("/carts/{cartId}/add/{toyId}")
    public ResponseEntity<Cart> addToyToCart(@PathVariable Long cartId, @PathVariable Long toyId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        Optional<Toy> optionalToy = toyRepository.findById(toyId);


        if (optionalCart.isPresent() && optionalToy.isPresent()) {
            Cart cart = optionalCart.get();
            Toy toy = optionalToy.get();

            cart.addToy(toy);
            return new ResponseEntity<>(cartRepository.save(cart), HttpStatus.OK);


        } else if(optionalCart.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/carts/{cartId}/remove/{toyId}")
    public ResponseEntity<Void> removeToyFromCart(@PathVariable Long cartId, @PathVariable Long toyId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if(optionalCart.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Cart cart = optionalCart.get();
        cart.removeToy(toyRepository.findById(toyId).orElse(null)); //Safe remove

        cartRepository.save(cart);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
