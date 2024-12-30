package com.ams.storeservice.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // User ID

    @ManyToMany
    @JoinTable(
            name = "cart_toy",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "toy_id")
    )
    private List<Toy> toys = new ArrayList<>();

    private boolean paid = false;

    // --- Getters and Setters ---

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

    // Getter for toys
    public List<Toy> getToys() {
        return toys;
    }

    // Setter for toys
    public void setToys(List<Toy> toys) {
        this.toys = toys;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    // --- Other methods ---

    public void addToy(Toy toy) {
        this.toys.add(toy);
    }

    public void removeToy(Toy toy) {
        this.toys.remove(toy);
    }
}