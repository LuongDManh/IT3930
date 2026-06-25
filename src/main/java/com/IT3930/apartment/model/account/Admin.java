package com.IT3930.apartment.model.account;
import com.IT3930.apartment.model.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "account_id")
public class Admin extends Account {
    public Admin() {
        setRole(Role.ADMIN);
    }
    public Admin(String email, String password, String phone, boolean isActive) {
        super(email, password, phone, isActive);
        setRole(Role.ADMIN);
    }
}
