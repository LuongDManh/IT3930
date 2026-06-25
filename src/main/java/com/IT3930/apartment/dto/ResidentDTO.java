package com.IT3930.apartment.dto;

import com.IT3930.apartment.model.Resident;

public class ResidentDTO {
    private Long id;
    private String name;
    private String cccd;

    public ResidentDTO() {}

    public ResidentDTO(Resident resident) {
        this.id = resident.getId();
        this.name = resident.getName();
        this.cccd = resident.getCccd();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
}
