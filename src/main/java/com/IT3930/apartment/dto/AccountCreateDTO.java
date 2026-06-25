package com.IT3930.apartment.dto;

import com.IT3930.apartment.model.Role;

public class AccountCreateDTO {
    private String email;
    private String password;
    private String phone;
    private Role role;

    public AccountCreateDTO() {
    }

    public AccountCreateDTO(String email, String password, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
