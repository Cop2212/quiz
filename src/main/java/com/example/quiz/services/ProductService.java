//package com.example.quiz.services;
//
//import com.example.quiz.entities.Question;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ProductService {
//    @Autowired
//    private ProductRepository productRepository;
//
//    public List<Question> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    public Question getProductById(Long id) {
//        return productRepository.findById(id).orElse(null);
//    }
//
//    public Question saveProduct(Question product) {
//        return productRepository.save(product);
//    }
//
//    public void deleteProduct(Long id) {
//        productRepository.deleteById(id);
//    }
//}
