package com.example.quiz.services;

import com.example.quiz.dto.UserDTO;
import com.example.quiz.entities.Profile;
import com.example.quiz.entities.User;
import com.example.quiz.repositories.ProfileRepository;
import com.example.quiz.repositories.UserRepository;
import com.example.quiz.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ProfileRepository profileRepository;

    public User signUp(UserDTO userDTO) {
        if ((userRepository.findByEmail(userDTO.getEmail()) == null) &&
                (userRepository.findByPhone(userDTO.getPhone()) == null)
        ) {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setPassword(PasswordEncoder.getInstance().encodePassword(userDTO.getPassword()));
            user.setActive(true);
            User result = userRepository.save(user);
            Profile profile = new Profile();
            profile.setAddress("");
            profile.setFirstName("");
            profile.setLastName("");
            profile.setUser(result);
            Profile profileResult = profileRepository.save(profile);
            return result;

        } else {
            return null;
        }
    }

    public User signIn(UserDTO userDTO) {

        //Hash password and compare
        User u = userRepository.findByEmail(userDTO.getEmail());
        if (u != null) {
            if (u.getPassword() == userDTO.getPassword()) {
                return u;
            }
        }

        return null;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        User result = userRepository.save(user);
        Profile profile = new Profile();
        profile.setAddress("");
        profile.setFirstName("");
        profile.setLastName("");
        profile.setUser(result);
        Profile profileResult = profileRepository.save(profile);
        return result;
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhone(userDetails.getPhone());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}