package com.IT3930.apartment.service;

import com.IT3930.apartment.model.Task;
import com.IT3930.apartment.model.account.Staff;
import com.IT3930.apartment.repository.TaskRepository;
import com.IT3930.apartment.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StaffRepository staffRepository;

    public List<Task> getTasksByStaff(Long staffId) {
        return taskRepository.findByStaffs_IdOrderByIdAsc(staffId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task, java.util.List<Long> staffIds) {
        if (staffIds != null && !staffIds.isEmpty()) {
            java.util.List<Staff> staffs = staffRepository.findAllById(staffIds);
            task.setStaffs(staffs);
        }
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task updatedTask, java.util.List<Long> staffIds) {
        Task existing = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setType(updatedTask.getType());
        existing.setIsDone(updatedTask.getIsDone());
        
        if (staffIds != null && !staffIds.isEmpty()) {
            java.util.List<Staff> staffs = staffRepository.findAllById(staffIds);
            existing.setStaffs(staffs);
        } else {
            existing.setStaffs(new java.util.ArrayList<>());
        }
        
        return taskRepository.save(existing);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
