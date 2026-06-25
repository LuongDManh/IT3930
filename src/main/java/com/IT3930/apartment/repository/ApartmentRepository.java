package com.IT3930.apartment.repository;

import com.IT3930.apartment.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    java.util.List<Apartment> findByOwnerIsNullOrderByIdAsc();
    java.util.List<Apartment> findByOwnerIdOrderByIdAsc(Long ownerId);
    java.util.List<Apartment> findAllByOrderByIdAsc();
}
