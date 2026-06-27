package com.IT3930.apartment.dto;

import com.IT3930.apartment.model.ServiceRequest;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceRequestDTO {
    private Long id;
    private Long amenityId;
    private String serviceName;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long apartmentId;
    private String apartmentName;
    private String apartmentFloor;
    private String ownerEmail;
    private Long taskId;
    private List<Long> staffIds;
    private List<String> staffNames;

    public ServiceRequestDTO() {}

    public ServiceRequestDTO(ServiceRequest request) {
        this.id = request.getId();
        this.serviceName = request.getServiceName();
        this.description = request.getDescription();
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
        this.updatedAt = request.getUpdatedAt();
        if (request.getApartment() != null) {
            this.apartmentId = request.getApartment().getId();
            this.apartmentName = request.getApartment().getName();
            this.apartmentFloor = request.getApartment().getFloor();
        }
        if (request.getOwner() != null) {
            this.ownerEmail = request.getOwner().getEmail();
        }
        if (request.getTask() != null) {
            this.taskId = request.getTask().getId();
            if (request.getTask().getStaffs() != null) {
                this.staffIds = request.getTask().getStaffs().stream()
                        .map(staff -> staff.getId())
                        .collect(java.util.stream.Collectors.toList());
                this.staffNames = request.getTask().getStaffs().stream()
                        .map(staff -> staff.getEmail())
                        .collect(java.util.stream.Collectors.toList());
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAmenityId() { return amenityId; }
    public void setAmenityId(Long amenityId) { this.amenityId = amenityId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Long getApartmentId() { return apartmentId; }
    public void setApartmentId(Long apartmentId) { this.apartmentId = apartmentId; }
    public String getApartmentName() { return apartmentName; }
    public void setApartmentName(String apartmentName) { this.apartmentName = apartmentName; }
    public String getApartmentFloor() { return apartmentFloor; }
    public void setApartmentFloor(String apartmentFloor) { this.apartmentFloor = apartmentFloor; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public List<Long> getStaffIds() { return staffIds; }
    public void setStaffIds(List<Long> staffIds) { this.staffIds = staffIds; }
    public List<String> getStaffNames() { return staffNames; }
    public void setStaffNames(List<String> staffNames) { this.staffNames = staffNames; }
}
