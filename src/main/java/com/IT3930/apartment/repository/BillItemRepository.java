package com.IT3930.apartment.repository;

import com.IT3930.apartment.model.bill.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {
    List<BillItem> findByIsActiveTrue();
}
