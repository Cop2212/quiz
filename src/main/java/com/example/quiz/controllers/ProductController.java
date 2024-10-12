//package com.example.quiz.controllers;
//
//import com.example.quiz.entities.Question;
//import com.example.quiz.services.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/products")
//public class ProductController {
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping
//    public List<Question> getAllProducts() {
//        return productService.getAllProducts();
//    }
//
//    @GetMapping("/{id}")
//    public Question getProductById(@PathVariable Long id) {
//        return productService.getProductById(id);
//    }
//
//    @PostMapping
//    public Question createProduct(@RequestBody Question product) {
//        return productService.saveProduct(product);
//    }
//
//    @PutMapping("/{id}")
//    public Question updateProduct(@PathVariable Long id, @RequestBody Question product) {
//        product.setId(id);
//        return productService.saveProduct(product);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//    }
//}
