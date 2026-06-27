package com.IT3930.apartment.service;

import com.IT3930.apartment.model.Apartment;
import com.IT3930.apartment.model.Resident;
import com.IT3930.apartment.repository.ApartmentRepository;
import com.IT3930.apartment.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;
    
    @Autowired
    private ResidentRepository residentRepository;

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAllByOrderByIdAsc();
    }

    public Apartment getApartmentById(Long id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));
    }

    @Transactional
    public Apartment saveApartment(Long id, String name, String floor, Double area) {
        if (name == null || name.isBlank() || floor == null || floor.isBlank() || area == null || area <= 0) {
            throw new IllegalArgumentException("Name, floor and a positive area are required.");
        }
        Apartment apartment = id == null ? new Apartment() : getApartmentById(id);
        apartment.setName(name.trim());
        apartment.setFloor(floor.trim());
        apartment.setArea(area);
        return apartmentRepository.save(apartment);
    }

    @Transactional
    public void deleteApartment(Long id) {
        Apartment apartment = getApartmentById(id);
        if (apartment.getOwner() != null || !residentRepository.findByApartmentId(id).isEmpty()) {
            throw new IllegalStateException("Remove the owner and residents before deleting this apartment.");
        }
        apartmentRepository.delete(apartment);
    }

    public List<Resident> getResidentsByApartment(Long apartmentId) {
        // verify apartment exists
        getApartmentById(apartmentId);
        return residentRepository.findByApartmentId(apartmentId);
    }

    @Transactional
    public Resident addResident(Long apartmentId, String name, String cccd) {
        Apartment apartment = getApartmentById(apartmentId);
        Resident resident = new Resident(name, cccd, apartment);
        return residentRepository.save(resident);
    }

    @Transactional
    public Resident updateResident(Long residentId, String name, String cccd) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found"));
        if (name != null) resident.setName(name);
        if (cccd != null) resident.setCccd(cccd);
        return residentRepository.save(resident);
    }

    @Transactional
    public void deleteResident(Long residentId) {
        residentRepository.deleteById(residentId);
    }
}
