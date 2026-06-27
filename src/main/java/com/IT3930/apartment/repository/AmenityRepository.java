package com.IT3930.apartment.repository;

import com.IT3930.apartment.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByActiveTrueOrderByNameAsc();
}
