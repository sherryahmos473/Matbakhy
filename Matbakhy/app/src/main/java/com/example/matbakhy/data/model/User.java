package com.example.matbakhy.data.model;

import java.util.Date;

public class User {
    private String uid;
    private String email;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    // REQUIRED: No-argument constructor for Firestore
    public User() {}

    // Constructor for manual creation
    public User(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}