package com.IT3930.apartment.service;

import com.IT3930.apartment.model.bill.BillItem;
import com.IT3930.apartment.repository.BillItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillItemService {

    @Autowired
    private BillItemRepository billItemRepository;

    // GET all active
    public List<BillItem> getAllBillItems() {
        return billItemRepository.findByIsActiveTrue();
    }

    // GET by ID
    public BillItem getBillItemById(Long id) {
        BillItem item = billItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillItem not found with id: " + id));
        if (!item.isActive()) {
            throw new RuntimeException("BillItem is inactive");
        }
        return item;
    }

    // ADD
    public BillItem addBillItem(BillItem billItem) {
        billItem.setActive(true);
        return billItemRepository.save(billItem);
    }

    // UPDATE
    public BillItem updateBillItem(Long id, BillItem updatedBillItem) {
        BillItem existingItem = getBillItemById(id);
        
        existingItem.setName(updatedBillItem.getName());
        existingItem.setUnitPrice(updatedBillItem.getUnitPrice());
        
        return billItemRepository.save(existingItem);
    }

    // DELETE (Soft delete)
    public void deleteBillItem(Long id) {
        BillItem existingItem = getBillItemById(id);
        existingItem.setActive(false);
        billItemRepository.save(existingItem);
    }
}
