package com.ams.paymentservice.controller;

import com.ams.paymentservice.dto.CartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class PaymentController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/pay/{cartId}")
    public ResponseEntity<String> pay(@PathVariable Long cartId) {
        try {
            // Assuming your store-service is running on port 8081
            String storeServiceUrl = "http://localhost:8081/carts/" + cartId;
            ResponseEntity<CartDTO> response = restTemplate.getForEntity(storeServiceUrl, CartDTO.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                CartDTO cartDTO = response.getBody();

                // Check if cartDTO is null
                if (cartDTO == null) {
                    return new ResponseEntity<>("Cart data is null", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                if (cartDTO.isPaid()) {
                    return new ResponseEntity<>("Cart is already paid", HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity<>("Payment successful for cart " + cartId, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
            }
        } catch (RestClientException e) {
            // Handle exceptions during the API call (e.g., store-service is down)
            return new ResponseEntity<>("Error communicating with store-service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}