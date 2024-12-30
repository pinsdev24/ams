package com.ams.paymentservice.dto;

import java.util.List;
import java.util.Objects;

public class CartDTO {

    private Long id;
    private Long userId;
    private List<ToyDTO> toys;
    private boolean paid;

    // Constructors
    public CartDTO(Long id, Long userId, List<ToyDTO> toys, boolean paid) {
        this.id = id;
        this.userId = userId;
        this.toys = toys;
        this.paid = paid;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ToyDTO> getToys() {
        return toys;
    }

    public void setToys(List<ToyDTO> toys) {
        this.toys = toys;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    // Calculate total price (optional, you can add other helper methods if needed)
    public double getTotalPrice() {
        return toys.stream().mapToDouble(ToyDTO::getPrice).sum();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartDTO cartDTO)) return false;
        return isPaid() == cartDTO.isPaid() && Objects.equals(getId(), cartDTO.getId()) && Objects.equals(getUserId(), cartDTO.getUserId()) && Objects.equals(getToys(), cartDTO.getToys());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getToys(), isPaid());
    }

}