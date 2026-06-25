package com.IT3930.apartment.service;

import com.IT3930.apartment.model.bill.BillUse;
import com.IT3930.apartment.repository.BillUseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BillUseService {

    @Autowired
    private BillUseRepository billUseRepository;

    // GET all
    public List<BillUse> getAllBillUses() {
        return billUseRepository.findAll();
    }

    // GET by ID
    public BillUse getBillUseById(Long id) {
        return billUseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillUse not found with id: " + id));
    }

    // ADD
    public BillUse addBillUse(BillUse billUse) {
        // "about month, when created, always take from current time"
        billUse.setMonth(YearMonth.now());
        if (billUse.getQuantity() == null) {
            billUse.setQuantity(0.0);
        }
        return billUseRepository.save(billUse);
    }

    // UPDATE
    public BillUse updateBillUse(Long id, BillUse updatedBillUse) {
        BillUse existingUse = billUseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillUse not found with id: " + id));
        
        existingUse.setQuantity(updatedBillUse.getQuantity());
        
        // Update references if they are provided
        if (updatedBillUse.getBillItem() != null) {
            existingUse.setBillItem(updatedBillUse.getBillItem());
        }
        if (updatedBillUse.getBill() != null) {
            existingUse.setBill(updatedBillUse.getBill());
        }
        
        return billUseRepository.save(existingUse);
    }

    // DELETE
    public void deleteBillUse(Long id) {
        BillUse existingUse = billUseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillUse not found with id: " + id));
        
        billUseRepository.delete(existingUse);
    }
}
