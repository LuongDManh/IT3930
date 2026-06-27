package com.IT3930.apartment.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "amenity")
public class Amenity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;
    @Column(nullable = false)
    private Integer quantity = 0;
    @Column(nullable = false)
    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
