package com.ams.storeservice.dto;

import java.util.Objects;

public class ToyDTO {

    private Long id;
    private String name;
    private String description;
    private double price;

    // Constructors
    public ToyDTO() {}

    public ToyDTO(Long id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToyDTO toyDTO)) return false;
        return Double.compare(getPrice(), toyDTO.getPrice()) == 0 && Objects.equals(getId(), toyDTO.getId()) && Objects.equals(getName(), toyDTO.getName()) && Objects.equals(getDescription(), toyDTO.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getPrice());
    }
}