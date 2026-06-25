package com.IT3930.apartment.service;

import com.IT3930.apartment.dto.ItemUsageDTO;
import com.IT3930.apartment.model.Apartment;
import com.IT3930.apartment.model.bill.Bill;
import com.IT3930.apartment.model.bill.BillItem;
import com.IT3930.apartment.model.bill.BillUse;
import com.IT3930.apartment.repository.ApartmentRepository;
import com.IT3930.apartment.repository.BillItemRepository;
import com.IT3930.apartment.repository.BillRepository;
import com.IT3930.apartment.repository.BillUseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillItemRepository billItemRepository;

    @Autowired
    private BillUseRepository billUseRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    // --- READ ---
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found: " + id));
    }

    // --- CREATE ---
    // 1. Initialize Bill with 0 quantity for all active BillItems
    @Transactional
    public Bill createBill(Long apartmentId, YearMonth month) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartment not found: " + apartmentId));

        YearMonth targetMonth = (month != null) ? month : YearMonth.now();
        
        // Prevent duplicate bills for the same month
        if (billRepository.existsByApartmentAndMonth(apartment, targetMonth)) {
            throw new RuntimeException("A bill for this month already exists for this apartment.");
        } else {
            // Create the Bill with 0 cost
            Bill bill = new Bill();
            bill.setApartment(apartment);
            bill.setDone(false);
            bill.setCost(BigDecimal.ZERO);
            bill.setMonth(targetMonth);
            
            bill = billRepository.save(bill);

            // Fetch all active BillItems (e.g. Water, Electricity, etc.)
            List<BillItem> allItems = billItemRepository.findByIsActiveTrue();
            List<BillUse> billUses = new ArrayList<>();
            BigDecimal totalCost = BigDecimal.ZERO;

            // Create a BillUse with default quantities
            for (BillItem item : allItems) {
                BillUse billUse = new BillUse();
                billUse.setBill(bill);
                billUse.setBillItem(item);
                
                double initialQty = 0.0;
                if (item.getName() != null && item.getName().toLowerCase().contains("management fee")) {
                    initialQty = apartment.getArea() != null ? apartment.getArea() : 0.0;
                }
                billUse.setQuantity(initialQty);
                billUse.setMonth(targetMonth);
                billUses.add(billUse);
                
                totalCost = totalCost.add(item.getUnitPrice().multiply(BigDecimal.valueOf(initialQty)));
            }

            bill.setCost(totalCost);
            bill = billRepository.save(bill); // Update cost
            billUseRepository.saveAll(billUses);
            return bill;
        }
    }

    // --- UPDATE ---
    // 2a. Update quantities from suppliers and calculate the total bill cost
    @Transactional
    public Bill updateQuantitiesAndCalculateBill(Long billId, List<ItemUsageDTO> usages) {
        Bill bill = getBillById(billId);
        List<BillUse> existingUses = billUseRepository.findByBill(bill);
        BigDecimal totalCost = BigDecimal.ZERO;

        // Update quantities based on supplier data
        for (BillUse billUse : existingUses) {
            
            for (ItemUsageDTO usageDTO : usages) {
                if (usageDTO.getBillItemId().equals(billUse.getBillItem().getId())) {
                    if (usageDTO.getQuantity() != null) {
                        billUse.setQuantity(usageDTO.getQuantity());
                    }
                    break;
                }
            }

            // Calculate cost for this item (quantity * unitPrice) and add to total
            BigDecimal itemCost = billUse.getBillItem().getUnitPrice().multiply(BigDecimal.valueOf(billUse.getQuantity()));
            totalCost = totalCost.add(itemCost);
        }

        billUseRepository.saveAll(existingUses);

        // Update the Bill's total cost
        bill.setCost(totalCost);
        return billRepository.save(bill);
    }

    // 2b. Standard update (e.g., mark as paid, change due date)
    @Transactional
    public Bill updateBill(Long id, Bill updatedBill) {
        Bill existingBill = getBillById(id);
        
        existingBill.setDone(updatedBill.isDone());
        if (updatedBill.getMonth() != null) {
            existingBill.setMonth(updatedBill.getMonth());
        }
        
        return billRepository.save(existingBill);
    }

    // --- DELETE ---
    @Transactional
    public void deleteBill(Long id) {
        Bill existingBill = getBillById(id);
        
        // First delete all associated BillUse records to avoid foreign key constraints
        List<BillUse> existingUses = billUseRepository.findByBill(existingBill);
        billUseRepository.deleteAll(existingUses);
        
        // Then delete the bill
        billRepository.delete(existingBill);
    }

    @Transactional
    public void deleteBillsByMonth(YearMonth month) {
        List<Bill> bills = billRepository.findByMonth(month);
        for (Bill bill : bills) {
            List<BillUse> existingUses = billUseRepository.findByBill(bill);
            billUseRepository.deleteAll(existingUses);
            billRepository.delete(bill);
        }
    }

    // 3. Create bills for all active apartments
    @Transactional
    public List<Bill> createBillsForAllApartments(YearMonth month) {
        List<Apartment> allApartments = apartmentRepository.findAll();
        List<Bill> createdBills = new ArrayList<>();
        YearMonth targetMonth = (month != null) ? month : YearMonth.now();

        for (Apartment apartment : allApartments) {
            if (!billRepository.existsByApartmentAndMonth(apartment, targetMonth)) {
                Bill bill = new Bill();
                bill.setApartment(apartment);
                bill.setDone(false);
                bill.setCost(BigDecimal.ZERO);
                bill.setMonth(targetMonth);
                
                bill = billRepository.save(bill);

                List<BillItem> allItems = billItemRepository.findByIsActiveTrue();
                List<BillUse> billUses = new ArrayList<>();
                BigDecimal totalCost = BigDecimal.ZERO;

                for (BillItem item : allItems) {
                    BillUse billUse = new BillUse();
                    billUse.setBill(bill);
                    billUse.setBillItem(item);
                    
                    double initialQty = 0.0;
                    if (item.getName() != null && item.getName().toLowerCase().contains("management fee")) {
                        initialQty = apartment.getArea() != null ? apartment.getArea() : 0.0;
                    }
                    billUse.setQuantity(initialQty);
                    billUse.setMonth(targetMonth);
                    billUses.add(billUse);
                    
                    totalCost = totalCost.add(item.getUnitPrice().multiply(BigDecimal.valueOf(initialQty)));
                }

                bill.setCost(totalCost);
                bill = billRepository.save(bill); // Update cost
                billUseRepository.saveAll(billUses);
                createdBills.add(bill);
            }
        }
        return createdBills;
    }
}
