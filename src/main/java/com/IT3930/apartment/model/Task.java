package com.IT3930.apartment.model;

import com.IT3930.apartment.model.account.Staff;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String type; // "STATIC" or "DYNAMIC"

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(nullable = false)
    private String status = "2"; // Default value to satisfy DB constraint

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "task_staff",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    @JsonIgnore
    private java.util.List<Staff> staffs = new java.util.ArrayList<>();

    public Task() {}

    public Task(String title, String description, String type, Boolean isDone, java.util.List<Staff> staffs) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.isDone = isDone;
        this.staffs = staffs;
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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public java.util.List<Staff> getStaffs() { return staffs; }
    public void setStaffs(java.util.List<Staff> staffs) { this.staffs = staffs; }
}
