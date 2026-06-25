package com.IT3930.apartment.model.account;
import com.IT3930.apartment.model.Apartment;
import com.IT3930.apartment.model.Role;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "owner")
@PrimaryKeyJoinColumn(name = "account_id")
public class Owner extends Account{
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Apartment> apartments = new ArrayList<>();

    public Owner() {
        super();
        setRole(Role.OWNER);
    }

    public Owner(String email, String password, String phone, boolean isActive) {
        super(email, password, phone, isActive);
        setRole(Role.OWNER);
    }

    public List<Apartment> getApartments() {
        return apartments;
    }
}
