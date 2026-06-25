package com.IT3930.apartment.dto;

import com.IT3930.apartment.model.Apartment;

public class ApartmentDTO {
    private Long id;
    private String name;
    private String floor;
    private Double area;

    public ApartmentDTO() {
    }

    public ApartmentDTO(Apartment apartment) {
        this.id = apartment.getId();
        this.name = apartment.getName();
        this.floor = apartment.getFloor();
        this.area = apartment.getArea();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}
