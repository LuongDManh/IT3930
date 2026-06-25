package com.IT3930.apartment.dto;

import com.IT3930.apartment.model.Task;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private Boolean isDone;
    private java.util.List<Long> staffIds;
    private java.util.List<String> staffNames;

    public TaskDTO() {}

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.type = task.getType();
        this.isDone = task.getIsDone();
        if (task.getStaffs() != null) {
            this.staffIds = task.getStaffs().stream()
                .map(s -> s.getId()).collect(java.util.stream.Collectors.toList());
            this.staffNames = task.getStaffs().stream()
                .map(s -> s.getEmail()).collect(java.util.stream.Collectors.toList());
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getIsDone() { return isDone; }
    public void setIsDone(Boolean isDone) { this.isDone = isDone; }

    public java.util.List<Long> getStaffIds() { return staffIds; }
    public void setStaffIds(java.util.List<Long> staffIds) { this.staffIds = staffIds; }

    public java.util.List<String> getStaffNames() { return staffNames; }
    public void setStaffNames(java.util.List<String> staffNames) { this.staffNames = staffNames; }
}
