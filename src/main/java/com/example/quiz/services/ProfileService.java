package com.example.quiz.services;

import com.example.quiz.entities.Profile;
import com.example.quiz.entities.User;
import com.example.quiz.repositories.ProfileRepository;
import com.example.quiz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public Profile getProfileByUserId(Long userId) {
        // Fetch the User by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return the Profile associated with the User
        return profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user"));
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }
}
