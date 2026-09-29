package com.example.androidminorproject;

public class ContactData {

    private String email;
    private String message;
    private String reply;

    // Default constructor required for Firestore
    public ContactData() {
    }

    public ContactData(String email, String message, String reply) {
        this.email = email;
        this.message = message;
        this.reply = reply;
    }

    // Getters and setters for the fields
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
