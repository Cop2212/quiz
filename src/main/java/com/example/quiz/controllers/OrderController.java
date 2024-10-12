//package com.example.quiz.controllers;
//
//import com.example.quiz.services.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/orders")
//public class OrderController {
//    @Autowired
//    private OrderService orderService;
//
//    @GetMapping
//    public List<Answer> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//
//    @GetMapping("/{id}")
//    public Answer getOrderById(@PathVariable Long id) {
//        return orderService.getOrderById(id);
//    }
//
//    @PostMapping
//    public Answer createOrder(@RequestBody Answer customerOrder) {
//        return orderService.saveOrder(customerOrder);
//    }
//
//    @PutMapping("/{id}")
//    public Answer updateOrder(@PathVariable Long id, @RequestBody Answer customerOrder) {
//        customerOrder.setId(id);
//        return orderService.saveOrder(customerOrder);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrder(id);
//    }
//}