package com.IT3930.apartment.repository;

import com.IT3930.apartment.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStaffs_IdOrderByIdAsc(Long staffId);
}
