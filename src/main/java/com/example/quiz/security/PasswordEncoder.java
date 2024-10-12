package com.example.quiz.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    private static PasswordEncoder instance;
    // Private constructor to prevent instantiation from outside the class
    public PasswordEncoder() {
    }
    // Method to get the singleton instance
    public static PasswordEncoder getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (PasswordEncoder.class) {
            if (instance == null) {
                instance = new PasswordEncoder();
            }
        }
        return instance;
    }
    public  String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedPassword = md.digest(password.getBytes());
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encoding password.", e);
        }
    }

    public   boolean matches(String raw, String hasPassword){

        System.out.println("encode:"+ encodePassword(raw));
        System.out.println("hasPassword:" + hasPassword);
        return encodePassword(raw).equals(hasPassword);
    }
    public  String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}