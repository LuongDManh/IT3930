package com.IT3930.apartment.repository;

import com.IT3930.apartment.model.ServiceRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    @EntityGraph(attributePaths = {"apartment", "owner", "task", "task.staffs"})
    List<ServiceRequest> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"apartment", "owner", "task", "task.staffs"})
    List<ServiceRequest> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    Optional<ServiceRequest> findByTaskId(Long taskId);
}
