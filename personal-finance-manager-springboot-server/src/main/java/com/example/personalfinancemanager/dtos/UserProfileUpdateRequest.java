package com.example.personalfinancemanager.dtos;

import jakarta.validation.constraints.NotBlank;

public class UserProfileUpdateRequest {

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    private String phoneNumber;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
