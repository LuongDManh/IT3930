package com.IT3930.apartment.model;
import jakarta.persistence.*;
import com.IT3930.apartment.model.account.Owner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apartment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"floor", "name"})
})
public class Apartment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "floor", nullable = false)
    private String floor;

    @Column(name = "area", nullable = false)
    private Double area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;

    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Resident> residents = new ArrayList<>();

    public Apartment(){
        super();
    }

    public Apartment(Long id, String floor, String name,Double area) {
        this.id = id;
        this.area = area;
        this.floor = floor;
        this.name = name;
    }

    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    public void setResidents(List<Resident> residents) {
        this.residents = residents;
    }
}
