//package com.example.quiz.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class OrderService {
//    @Autowired
//    private OrderRepository orderRepository;
//
//    public List<Answer> getAllOrders() {
//        return orderRepository.findAll();
//    }
//
//    public Answer getOrderById(Long id) {
//        return orderRepository.findById(id).orElse(null);
//    }
//
//    public Answer saveOrder(Answer customerOrder) {
//        return orderRepository.save(customerOrder);
//    }
//
//    public void deleteOrder(Long id) {
//        orderRepository.deleteById(id);
//    }
//}
