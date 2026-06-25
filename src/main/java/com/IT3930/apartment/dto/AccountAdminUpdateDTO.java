package com.IT3930.apartment.dto;

public class AccountAdminUpdateDTO {
    private String email;
    private String password;
    private String phone;
    @com.fasterxml.jackson.annotation.JsonProperty("isActive")
    private Boolean isActive;

    public AccountAdminUpdateDTO() {
    }

    public AccountAdminUpdateDTO(String email, String password, String phone, Boolean isActive) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isActive = isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
