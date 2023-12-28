package com.example.bookhub.models;

public class User {
    private String fullName ;
    private String email ;

    private String imageUri ;

    private String deviceTokens ;

    public User() {
    }

    public User(String fullName, String email, String imageUri, String deviceTokens) {
        this.fullName = fullName;
        this.email = email;
        this.imageUri = imageUri;
        this.deviceTokens = deviceTokens;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDeviceTokens() {
        return deviceTokens;
    }

    public void setDeviceTokens(String deviceTokens) {
        this.deviceTokens = deviceTokens;
    }
}
