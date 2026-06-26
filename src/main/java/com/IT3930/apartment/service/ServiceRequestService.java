package com.IT3930.apartment.service;

import com.IT3930.apartment.dto.ServiceRequestDTO;
import com.IT3930.apartment.model.Apartment;
import com.IT3930.apartment.model.ServiceRequest;
import com.IT3930.apartment.model.Task;
import com.IT3930.apartment.model.account.Account;
import com.IT3930.apartment.model.account.Owner;
import com.IT3930.apartment.model.account.Staff;
import com.IT3930.apartment.repository.ApartmentRepository;
import com.IT3930.apartment.repository.ServiceRequestRepository;
import com.IT3930.apartment.repository.StaffRepository;
import com.IT3930.apartment.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;
    private final ApartmentRepository apartmentRepository;
    private final StaffRepository staffRepository;
    private final TaskRepository taskRepository;

    public ServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            ApartmentRepository apartmentRepository,
            StaffRepository staffRepository,
            TaskRepository taskRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.apartmentRepository = apartmentRepository;
        this.staffRepository = staffRepository;
        this.taskRepository = taskRepository;
    }

    public List<ServiceRequest> getAllRequests() {
        return serviceRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<ServiceRequest> getRequestsByOwner(Long ownerId) {
        return serviceRequestRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    @Transactional
    public ServiceRequest createRequest(Account account, ServiceRequestDTO requestDTO) {
        if (!(account instanceof Owner owner)) {
            throw new IllegalArgumentException("Only owners can create service requests.");
        }

        Apartment apartment = apartmentRepository.findById(requestDTO.getApartmentId())
                .orElseThrow(() -> new RuntimeException("Apartment not found: " + requestDTO.getApartmentId()));

        if (apartment.getOwner() == null || !apartment.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("You can only create requests for your own apartment.");
        }

        ServiceRequest request = new ServiceRequest();
        request.setApartment(apartment);
        request.setOwner(owner);
        request.setServiceName(requestDTO.getServiceName());
        request.setDescription(requestDTO.getDescription());
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        return serviceRequestRepository.save(request);
    }

    @Transactional
    public ServiceRequest assignRequest(Long requestId, List<Long> staffIds) {
        ServiceRequest request = getRequest(requestId);
        List<Staff> staffs = staffIds == null ? new ArrayList<>() : staffRepository.findAllById(staffIds);

        Task task = request.getTask();
        if (task == null) {
            task = new Task();
            task.setType("DYNAMIC");
            task.setIsDone(false);
        }

        task.setTitle("Service request: " + request.getServiceName());
        task.setDescription(buildTaskDescription(request));
        task.setStaffs(staffs);
        task = taskRepository.save(task);

        request.setTask(task);
        request.setStatus(staffs.isEmpty() ? "PENDING" : "ASSIGNED");
        request.setUpdatedAt(LocalDateTime.now());
        return serviceRequestRepository.save(request);
    }

    @Transactional
    public ServiceRequest updateStatus(Long requestId, String status) {
        ServiceRequest request = getRequest(requestId);
        request.setStatus(status);
        request.setUpdatedAt(LocalDateTime.now());

        if (request.getTask() != null && ("DONE".equals(status) || "ASSIGNED".equals(status))) {
            request.getTask().setIsDone("DONE".equals(status));
            taskRepository.save(request.getTask());
        }

        return serviceRequestRepository.save(request);
    }

    @Transactional
    public void completeTask(Long taskId, Long staffId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        boolean assigned = task.getStaffs().stream().anyMatch(staff -> staff.getId().equals(staffId));
        if (!assigned) {
            throw new IllegalArgumentException("This task is not assigned to the current staff.");
        }

        task.setIsDone(true);
        taskRepository.save(task);

        serviceRequestRepository.findByTaskId(taskId).ifPresent(request -> {
            request.setStatus("DONE");
            request.setUpdatedAt(LocalDateTime.now());
            serviceRequestRepository.save(request);
        });
    }

    private ServiceRequest getRequest(Long requestId) {
        return serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Service request not found: " + requestId));
    }

    private String buildTaskDescription(ServiceRequest request) {
        String apartmentLabel = request.getApartment().getName() + " - Floor " + request.getApartment().getFloor();
        String ownerLabel = request.getOwner().getEmail();
        String description = request.getDescription() == null ? "" : request.getDescription();
        return "Apartment: " + apartmentLabel + "\nOwner: " + ownerLabel + "\nRequest: " + description;
    }
}
