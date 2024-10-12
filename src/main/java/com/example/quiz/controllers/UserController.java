//package com.example.quiz.controllers;
//
//
//import com.example.quiz.dto.ResponseObject;
//import com.example.quiz.entities.User;
//import com.example.quiz.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    // Get all users
//    @GetMapping
//    public ResponseEntity<?> getAllUsers() {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(
//                        new ResponseObject(200, "OK",
//                                userService.getAllUsers()));
//    }
//
//    // Get user by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable Long id) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(
//                        new ResponseObject(200, "OK",
//                                userService.getUserById(id)
//                                        .map(ResponseEntity::ok)
//                                        .orElse(ResponseEntity.notFound().build())));
//    }
//
//    // Create a new user
//    @PostMapping
//    public ResponseEntity<?> createUser(@RequestBody User user) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(
//                        new ResponseObject(200, "OK",
//                                userService.createUser(user)));
//
//
//    }
//
//    // Update user
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(
//                        new ResponseObject(200, "OK",
//                                userService.updateUser(id, userDetails)));
//
//    }
//
//    // Delete user
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//}