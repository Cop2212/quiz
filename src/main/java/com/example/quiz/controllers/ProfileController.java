//package com.example.quiz.controllers;
//
//import com.example.quiz.entities.Quiz;
//import com.example.quiz.services.ProfileService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/profiles")
//public class ProfileController {
//    @Autowired
//    private ProfileService profileService;
//
//    @GetMapping
//    public List<Quiz> getAllProfiles() {
//        return profileService.getAllProfiles();
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Quiz> getProfileByUserId(@PathVariable Long userId) {
//        // Fetch the profile using the service method
//        Quiz profile = profileService.getProfileByUserId(userId);
//        return ResponseEntity.ok(profile);
//    }
//
//    @GetMapping("/{id}")
//    public Quiz getProfileById(@PathVariable Long id) {
//        return profileService.getProfileById(id);
//    }
//
//    @PostMapping
//    public Quiz createProfile(@RequestBody Quiz profile) {
//        return profileService.saveProfile(profile);
//    }
//
//    @PutMapping("/{id}")
//    public Quiz updateProfile(@PathVariable Long id, @RequestBody Quiz profile) {
//        profile.setId(id);
//        return profileService.saveProfile(profile);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteProfile(@PathVariable Long id) {
//        profileService.deleteProfile(id);
//    }
//}