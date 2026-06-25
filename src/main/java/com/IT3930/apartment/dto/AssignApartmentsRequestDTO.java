package com.IT3930.apartment.dto;

import java.util.List;

public class AssignApartmentsRequestDTO {
    private List<Long> apartmentIds;

    public AssignApartmentsRequestDTO() {
    }

    public List<Long> getApartmentIds() {
        return apartmentIds;
    }

    public void setApartmentIds(List<Long> apartmentIds) {
        this.apartmentIds = apartmentIds;
    }
}
