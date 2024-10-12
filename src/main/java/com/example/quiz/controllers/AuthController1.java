//package com.example.quiz.controllers;
//
//import com.example.quiz.dto.ResponseObject;
//import com.example.quiz.dto.UserDTO;
//import com.example.quiz.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController1 {
//
//
//    @Autowired
//    UserService userService;
//
//
//    @PostMapping("/signUp")
//    public ResponseEntity<?> signUp(@RequestBody UserDTO user) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(
//                        new ResponseObject(200, "OK",
//                                userService.signUp(user)));
//
//
//    }
//
//    @PostMapping("/signIn")
//    public ResponseEntity<?> signIn(@RequestBody UserDTO user) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(
//                        new ResponseObject(200, "OK",
//                                userService.signIn(user)));
//
//    }
//
//
//}