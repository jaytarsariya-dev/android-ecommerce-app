package com.example.androidminorproject;

public class TeamMember {
    private String name;
    private String role;
    private int photoResId;

    // Constructor
    public TeamMember(String name, String role, int photoResId) {
        this.name = name;
        this.role = role;
        this.photoResId = photoResId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getPhotoResId() {
        return photoResId;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhotoResId(int photoResId) {
        this.photoResId = photoResId;
    }

}
