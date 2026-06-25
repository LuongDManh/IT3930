package com.IT3930.apartment.controller;

import com.IT3930.apartment.dto.ApartmentDetailDTO;
import com.IT3930.apartment.dto.ResidentDTO;
import com.IT3930.apartment.model.Apartment;
import com.IT3930.apartment.model.Role;
import com.IT3930.apartment.model.account.Account;
import com.IT3930.apartment.security.CustomUserDetails;
import com.IT3930.apartment.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<?> getAllApartments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getAccount().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied. Only admin can view all blocks.");
        }
        List<ApartmentDetailDTO> apartments = apartmentService.getAllApartments()
                .stream()
                .map(ApartmentDetailDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApartmentById(@PathVariable Long id) {
        try {
            Apartment apt = apartmentService.getApartmentById(id);
            return ResponseEntity.ok(new ApartmentDetailDTO(apt));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/residents")
    public ResponseEntity<?> getResidentsByApartment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Account account = userDetails.getAccount();
            Apartment apt = apartmentService.getApartmentById(id);
            
            if (account.getRole() == Role.OWNER) {
                if (apt.getOwner() == null || !apt.getOwner().getId().equals(account.getId())) {
                    return ResponseEntity.status(403).body("Access denied. You do not own this block.");
                }
            } else if (account.getRole() != Role.ADMIN) {
                return ResponseEntity.status(403).body("Access denied.");
            }

            List<ResidentDTO> residents = apartmentService.getResidentsByApartment(id)
                    .stream()
                    .map(ResidentDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(residents);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/residents")
    public ResponseEntity<?> addResident(
            @PathVariable Long id,
            @RequestBody ResidentDTO residentDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getAccount().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied. Only admin can add residents.");
        }
        try {
            apartmentService.addResident(id, residentDTO.getName(), residentDTO.getCccd());
            return ResponseEntity.ok("Resident added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/residents/{residentId}")
    public ResponseEntity<?> updateResident(
            @PathVariable Long residentId,
            @RequestBody ResidentDTO residentDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getAccount().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied. Only admin can update residents.");
        }
        try {
            apartmentService.updateResident(residentId, residentDTO.getName(), residentDTO.getCccd());
            return ResponseEntity.ok("Resident updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/residents/{residentId}")
    public ResponseEntity<?> deleteResident(
            @PathVariable Long residentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getAccount().getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied. Only admin can delete residents.");
        }
        try {
            apartmentService.deleteResident(residentId);
            return ResponseEntity.ok("Resident deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
