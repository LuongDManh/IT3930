package com.IT3930.apartment.dto;

import com.IT3930.apartment.model.Role;
import com.IT3930.apartment.model.account.Account;

public class AccountResponseDTO {
    private Long id;
    private String email;
    private String phone;
    @com.fasterxml.jackson.annotation.JsonProperty("isActive")
    private boolean isActive;
    private Role role;

    public AccountResponseDTO() {
    }

    public AccountResponseDTO(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.isActive = account.isActive();
        this.role = account.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
