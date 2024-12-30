package com.ams.storeservice.controller;

import com.ams.storeservice.entity.Cart;
import com.ams.storeservice.entity.Toy;
import com.ams.storeservice.repository.CartRepository;
import com.ams.storeservice.repository.ToyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ToyRepository toyRepository;

    @MockitoBean
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        // create fake data
        Toy toy1 = new Toy();
        toy1.setId(1L);
        toy1.setName("Test Toy 1");
        toy1.setDescription("Description of Test Toy 1");
        toy1.setPrice(20);

        Toy toy2 = new Toy();
        toy2.setId(2L);
        toy2.setName("Test Toy 2");
        toy2.setDescription("Description of Test Toy 2");
        toy2.setPrice(30);

        when(toyRepository.findAll()).thenReturn(Arrays.asList(toy1, toy2));
    }

    @Test
    void getAllToys_ShouldReturnListOfToys() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/toys"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Test Toy 1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Test Toy 2"));

        Mockito.verify(toyRepository).findAll();
    }

    @Test
    void createCart_ShouldCreateNewCart() throws Exception {
        // Arrange
        Long userId = 1L;
        Cart newCart = new Cart();
        newCart.setId(1L);
        newCart.setUserId(userId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // Act & Assert
        mockMvc.perform(post("/toys/carts/{userId}", userId))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    void addToyToCart_ShouldAddToyAndReturnCart() throws Exception {
        // Arrange
        Long cartId = 1L;
        Long toyId = 1L;
        Cart existingCart = new Cart();
        existingCart.setId(cartId);
        existingCart.setUserId(1L);

        Toy toyToAdd = new Toy();
        toyToAdd.setId(toyId);
        toyToAdd.setDescription("Description of Toy ");
        toyToAdd.setName("Test Toy");
        toyToAdd.setPrice(20);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        when(toyRepository.findById(toyId)).thenReturn(Optional.of(toyToAdd));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        mockMvc.perform(post("/toys/carts/{cartId}/add/{toyId}", cartId, toyId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.toys[0].id").value(toyId));
    }

    @Test
    void removeToyFromCart_ShouldRemoveToyAndReturnNoContent() throws Exception {
        // Arrange
        Long cartId = 1L;
        Long toyId = 1L;
        Cart existingCart = new Cart();
        existingCart.setId(cartId);
        existingCart.setUserId(1L);

        Toy toyToRemove = new Toy();
        toyToRemove.setId(toyId);
        toyToRemove.setName("Test Toy");
        existingCart.addToy(toyToRemove);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        when(toyRepository.findById(toyId)).thenReturn(Optional.of(toyToRemove));

        // Act & Assert
        mockMvc.perform(delete("/toys/carts/{cartId}/remove/{toyId}", cartId, toyId))
                .andExpect(status().isNoContent());
    }
}