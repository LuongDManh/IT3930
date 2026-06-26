package com.IT3930.apartment.controller;

import com.IT3930.apartment.dto.TaskDTO;
import com.IT3930.apartment.security.CustomUserDetails;
import com.IT3930.apartment.service.ServiceRequestService;
import com.IT3930.apartment.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ServiceRequestService serviceRequestService;

    @GetMapping("/tasks")
    public ResponseEntity<?> getMyTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long staffId = userDetails.getAccount().getId();
            List<TaskDTO> tasks = taskService.getTasksByStaff(staffId)
                    .stream()
                    .map(TaskDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/tasks/{id}/complete")
    public ResponseEntity<?> completeTask(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            serviceRequestService.completeTask(id, userDetails.getAccount().getId());
            return ResponseEntity.ok("Task completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
