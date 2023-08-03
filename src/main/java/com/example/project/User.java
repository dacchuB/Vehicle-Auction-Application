package com.example.project;

public class User {
    private String username;
    private String email;
    // Add additional fields as needed

    public User() {
        // Default constructor required for Firebase
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // Add setters if necessary
}

