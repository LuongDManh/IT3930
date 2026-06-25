package com.IT3930.apartment.repository;

import com.IT3930.apartment.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByApartmentId(Long apartmentId);
}
