package com.IT3930.apartment.service;

import com.IT3930.apartment.model.Amenity;
import com.IT3930.apartment.repository.AmenityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AmenityService {
    private final AmenityRepository repository;

    public AmenityService(AmenityRepository repository) { this.repository = repository; }
    public List<Amenity> getAll() { return repository.findAll(); }
    public List<Amenity> getActive() { return repository.findByActiveTrueOrderByNameAsc(); }

    @Transactional
    public Amenity save(Long id, Amenity input) {
        Amenity amenity = id == null ? new Amenity() : repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found."));
        if (input.getName() == null || input.getName().isBlank()) {
            throw new IllegalArgumentException("Service name is required.");
        }
        amenity.setName(input.getName().trim());
        amenity.setUnitPrice(input.getUnitPrice() == null ? BigDecimal.ZERO : input.getUnitPrice());
        amenity.setQuantity(input.getQuantity() == null ? 0 : input.getQuantity());
        amenity.setActive(input.isActive());
        return repository.save(amenity);
    }

    public void delete(Long id) { repository.deleteById(id); }
}
