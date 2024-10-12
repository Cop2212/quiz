package com.example.quiz.controllers;

import com.example.quiz.entities.CustomerOrder;
import com.example.quiz.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<CustomerOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public CustomerOrder getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public CustomerOrder createOrder(@RequestBody CustomerOrder customerOrder) {
        return orderService.saveOrder(customerOrder);
    }

    @PutMapping("/{id}")
    public CustomerOrder updateOrder(@PathVariable Long id, @RequestBody CustomerOrder customerOrder) {
        customerOrder.setId(id);
        return orderService.saveOrder(customerOrder);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}