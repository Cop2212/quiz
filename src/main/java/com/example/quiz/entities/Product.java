package com.example.quiz.entities;

import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;

@Entity

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    @ManyToMany(mappedBy = "products")
    private Set<CustomerOrder> customerOrders = new HashSet<>();

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<CustomerOrder> getOrders() {
        return customerOrders;
    }

    public void setOrders(Set<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }
}
