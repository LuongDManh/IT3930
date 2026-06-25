package com.IT3930.apartment.repository;

import com.IT3930.apartment.model.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @EntityGraph(attributePaths = {"apartment"})
    @Override
    java.util.List<Bill> findAll();

    boolean existsByApartmentAndMonth(com.IT3930.apartment.model.Apartment apartment, java.time.YearMonth month);

    @EntityGraph(attributePaths = {"apartment"})
    java.util.List<Bill> findByMonth(java.time.YearMonth month);

    @EntityGraph(attributePaths = {"apartment"})
    java.util.List<Bill> findByApartmentIn(java.util.List<com.IT3930.apartment.model.Apartment> apartments);
}
