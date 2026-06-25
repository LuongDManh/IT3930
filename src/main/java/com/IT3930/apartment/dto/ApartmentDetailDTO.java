package com.IT3930.apartment.dto;

import com.IT3930.apartment.model.Apartment;

public class ApartmentDetailDTO {
    private Long id;
    private String name;
    private String floor;
    private Double area;
    private String ownerEmail;
    private Long ownerId;

    public ApartmentDetailDTO() {}

    public ApartmentDetailDTO(Apartment apartment) {
        this.id = apartment.getId();
        this.name = apartment.getName();
        this.floor = apartment.getFloor();
        this.area = apartment.getArea();
        if (apartment.getOwner() != null) {
            this.ownerEmail = apartment.getOwner().getEmail();
            this.ownerId = apartment.getOwner().getId();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
