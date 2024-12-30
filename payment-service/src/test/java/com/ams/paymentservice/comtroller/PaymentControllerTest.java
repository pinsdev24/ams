package com.ams.paymentservice.comtroller;

import com.ams.paymentservice.dto.CartDTO;
import com.ams.paymentservice.dto.ToyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestTemplate restTemplate;

    private CartDTO cartDTO;

    @BeforeEach
    void setUp() {
        // Create a sample CartDTO for testing
        ToyDTO toy = new ToyDTO(1L, "Test Toy", "A description", 10.0);
        cartDTO = new CartDTO(1L, 1L, Collections.singletonList(toy), false);
    }

    @Test
    void pay_whenCartExistsAndNotPaid_shouldReturnOkAndMarkPaid() throws Exception {
        // Arrange
        Long cartId = 1L;

        // Mock the RestTemplate to return the CartDTO
        when(restTemplate.getForEntity(anyString(), eq(CartDTO.class)))
                .thenReturn(new ResponseEntity<>(cartDTO, HttpStatus.OK));

        // Act & Assert
        mockMvc.perform(post("/pay/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment successful for cart " + cartId));
    }

    @Test
    void pay_whenCartNotFound_shouldReturnNotFound() throws Exception {
        // Arrange
        Long cartId = 1L;

        // Mock the RestTemplate to return a 404 response
        when(restTemplate.getForEntity(anyString(), eq(CartDTO.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Act & Assert
        mockMvc.perform(post("/pay/{cartId}", cartId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cart not found"));
    }

    @Test
    void pay_whenCartAlreadyPaid_shouldReturnBadRequest() throws Exception {
        // Arrange
        Long cartId = 1L;

        // Set the cart as already paid
        cartDTO.setPaid(true);

        // Mock the RestTemplate to return the CartDTO
        when(restTemplate.getForEntity(anyString(), eq(CartDTO.class)))
                .thenReturn(new ResponseEntity<>(cartDTO, HttpStatus.OK));

        // Act & Assert
        mockMvc.perform(post("/pay/{cartId}", cartId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cart is already paid"));
    }

    @Test
    void pay_whenStoreServiceIsDown_shouldReturnInternalServerError() throws Exception {
        // Arrange
        Long cartId = 1L;

        // Mock the RestTemplate to throw an exception
        when(restTemplate.getForEntity(anyString(), eq(CartDTO.class)))
                .thenThrow(new RestClientException("Simulated error"));

        // Act & Assert
        mockMvc.perform(post("/pay/{cartId}", cartId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error communicating with store-service"));
    }
}