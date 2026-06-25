package com.IT3930.apartment.controller;

import com.IT3930.apartment.dto.ApartmentDTO;
import com.IT3930.apartment.security.CustomUserDetails;
import com.IT3930.apartment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/apartments")
    public ResponseEntity<?> getMyApartments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long ownerId = userDetails.getAccount().getId();
            List<ApartmentDTO> apartments = accountService.getApartmentsByOwner(ownerId)
                    .stream()
                    .map(ApartmentDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(apartments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Autowired
    private com.IT3930.apartment.repository.BillRepository billRepository;

    @Autowired
    private com.IT3930.apartment.repository.BillUseRepository billUseRepository;

    @GetMapping("/bills")
    public ResponseEntity<?> getMyBills(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long ownerId = userDetails.getAccount().getId();
            List<com.IT3930.apartment.model.Apartment> apartments = accountService.getApartmentsByOwner(ownerId);
            
            if (apartments.isEmpty()) {
                return ResponseEntity.ok(java.util.Collections.emptyList());
            }

            List<com.IT3930.apartment.model.bill.Bill> bills = billRepository.findByApartmentIn(apartments);
            List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();

            List<com.IT3930.apartment.model.bill.BillUse> allUses = bills.isEmpty() ? new java.util.ArrayList<>() : billUseRepository.findByBillIn(bills);
            java.util.Map<Long, List<com.IT3930.apartment.model.bill.BillUse>> usesByBill = allUses.stream()
                    .collect(java.util.stream.Collectors.groupingBy(u -> u.getBill().getId()));

            for (com.IT3930.apartment.model.bill.Bill bill : bills) {
                java.util.Map<String, Object> billMap = new java.util.HashMap<>();
                billMap.put("billId", bill.getId());
                billMap.put("apartmentId", bill.getApartment().getId());
                billMap.put("apartmentName", bill.getApartment().getName());
                billMap.put("apartmentFloor", bill.getApartment().getFloor());
                billMap.put("cost", bill.getCost());
                billMap.put("isDone", bill.isDone());
                billMap.put("month", bill.getMonth());

                List<com.IT3930.apartment.model.bill.BillUse> uses = usesByBill.getOrDefault(bill.getId(), new java.util.ArrayList<>());
                List<java.util.Map<String, Object>> items = new java.util.ArrayList<>();
                for (com.IT3930.apartment.model.bill.BillUse use : uses) {
                    java.util.Map<String, Object> itemMap = new java.util.HashMap<>();
                    itemMap.put("itemName", use.getBillItem().getName());
                    itemMap.put("quantity", use.getQuantity());
                    items.add(itemMap);
                }
                billMap.put("totalItems", items.size());
                billMap.put("items", items);

                result.add(billMap);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
