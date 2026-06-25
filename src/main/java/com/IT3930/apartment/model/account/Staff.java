package com.IT3930.apartment.model.account;
import com.IT3930.apartment.model.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "staff")
@PrimaryKeyJoinColumn(name = "account_id")
public class Staff extends Account {
    @ManyToMany(mappedBy = "staffs", fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<com.IT3930.apartment.model.Task> tasks = new java.util.ArrayList<>();

    public Staff() {
        setRole(Role.STAFF);
    }
    public Staff(String email, String password, String phone, boolean isActive) {
        super(email, password, phone, isActive);
        setRole(Role.STAFF);
    }

    public java.util.List<com.IT3930.apartment.model.Task> getTasks() {
        return tasks;
    }
}
