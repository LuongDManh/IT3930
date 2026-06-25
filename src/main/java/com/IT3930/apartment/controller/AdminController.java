package com.IT3930.apartment.controller;

import com.IT3930.apartment.dto.AccountCreateDTO;
import com.IT3930.apartment.dto.AccountResponseDTO;
import com.IT3930.apartment.dto.AccountAdminUpdateDTO;
import com.IT3930.apartment.dto.ApartmentDTO;
import com.IT3930.apartment.dto.AssignApartmentsRequestDTO;
import com.IT3930.apartment.model.account.Account;
import com.IT3930.apartment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.IT3930.apartment.dto.BillGenerationRequestDTO;
import com.IT3930.apartment.model.bill.Bill;
import com.IT3930.apartment.model.bill.BillItem;
import com.IT3930.apartment.model.bill.BillUse;
import com.IT3930.apartment.service.BillItemService;
import com.IT3930.apartment.service.BillService;
import com.IT3930.apartment.service.BillUseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BillService billService;

    @Autowired
    private BillItemService billItemService;

    @Autowired
    private BillUseService billUseService;

    // Only admins should access this, which can be protected by Spring Security
    // e.g. @PreAuthorize("hasRole('ADMIN')") or via SecurityConfig matchers
    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@RequestBody AccountCreateDTO createDTO) {
        try {
            Account newAccount = accountService.createAccount(
                    createDTO.getEmail(),
                    createDTO.getPassword(),
                    createDTO.getPhone(),
                    createDTO.getRole()
            );
            return ResponseEntity.ok(new AccountResponseDTO(newAccount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts() {
        try {
            List<AccountResponseDTO> accounts = accountService.getAllAccounts()
                    .stream()
                    .map(AccountResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<?> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountAdminUpdateDTO updateDTO) {
        try {
            Account updatedAccount = accountService.updateAccountByAdmin(
                    id,
                    updateDTO.getEmail(),
                    updateDTO.getPassword(),
                    updateDTO.getPhone(),
                    updateDTO.getIsActive()
            );
            return ResponseEntity.ok(new AccountResponseDTO(updatedAccount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/apartments/available")
    public ResponseEntity<?> getAvailableApartments() {
        try {
            List<ApartmentDTO> apartments = accountService.getAvailableApartments()
                    .stream()
                    .map(ApartmentDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(apartments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/accounts/{id}/apartments")
    public ResponseEntity<?> getApartmentsByOwner(@PathVariable Long id) {
        try {
            List<ApartmentDTO> apartments = accountService.getApartmentsByOwner(id)
                    .stream()
                    .map(ApartmentDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(apartments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/accounts/{id}/apartments")
    public ResponseEntity<?> assignApartments(
            @PathVariable Long id,
            @RequestBody AssignApartmentsRequestDTO request) {
        try {
            accountService.assignApartmentsToOwner(id, request.getApartmentIds());
            return ResponseEntity.ok("Apartments assigned successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- BILLS ---
    @GetMapping("/bills")
    public ResponseEntity<?> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @PostMapping("/bills")
    public ResponseEntity<?> createBill(@RequestBody BillGenerationRequestDTO request) {
        try {
            Bill newBill = billService.createBill(request.getApartmentId(), request.getMonth());
            return ResponseEntity.ok(newBill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bills/bulk")
    public ResponseEntity<?> createBillsForAllApartments(@RequestBody BillGenerationRequestDTO request) {
        try {
            List<Bill> newBills = billService.createBillsForAllApartments(request.getMonth());
            return ResponseEntity.ok(newBills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/bills/month/{month}")
    public ResponseEntity<?> deleteBillsByMonth(@PathVariable java.time.YearMonth month) {
        try {
            billService.deleteBillsByMonth(month);
            return ResponseEntity.ok("Bills deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/bills/{id}/usages")
    public ResponseEntity<?> updateBillUsages(@PathVariable Long id, @RequestBody BillGenerationRequestDTO request) {
        try {
            Bill updatedBill = billService.updateQuantitiesAndCalculateBill(id, request.getUsages());
            return ResponseEntity.ok(updatedBill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/bills/{id}")
    public ResponseEntity<?> updateBill(@PathVariable Long id, @RequestBody Bill updatedBill) {
        try {
            Bill savedBill = billService.updateBill(id, updatedBill);
            return ResponseEntity.ok(savedBill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/bills/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Long id) {
        try {
            billService.deleteBill(id);
            return ResponseEntity.ok("Bill deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- BILL ITEMS ---
    @GetMapping("/bill-items")
    public ResponseEntity<?> getAllBillItems() {
        return ResponseEntity.ok(billItemService.getAllBillItems());
    }

    @PostMapping("/bill-items")
    public ResponseEntity<?> addBillItem(@RequestBody BillItem billItem) {
        return ResponseEntity.ok(billItemService.addBillItem(billItem));
    }

    @PutMapping("/bill-items/{id}")
    public ResponseEntity<?> updateBillItem(@PathVariable Long id, @RequestBody BillItem updatedBillItem) {
        try {
            return ResponseEntity.ok(billItemService.updateBillItem(id, updatedBillItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/bill-items/{id}")
    public ResponseEntity<?> deleteBillItem(@PathVariable Long id) {
        try {
            billItemService.deleteBillItem(id);
            return ResponseEntity.ok("Bill item deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- BILL USES ---
    @GetMapping("/bill-uses")
    public ResponseEntity<?> getAllBillUses() {
        return ResponseEntity.ok(billUseService.getAllBillUses());
    }

    @PostMapping("/bill-uses")
    public ResponseEntity<?> addBillUse(@RequestBody BillUse billUse) {
        return ResponseEntity.ok(billUseService.addBillUse(billUse));
    }

    @PutMapping("/bill-uses/{id}")
    public ResponseEntity<?> updateBillUse(@PathVariable Long id, @RequestBody BillUse updatedBillUse) {
        try {
            return ResponseEntity.ok(billUseService.updateBillUse(id, updatedBillUse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/bill-uses/{id}")
    public ResponseEntity<?> deleteBillUse(@PathVariable Long id) {
        try {
            billUseService.deleteBillUse(id);
            return ResponseEntity.ok("Bill use deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // --- TASKS ---
    @Autowired
    private com.IT3930.apartment.service.TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks() {
        try {
            List<com.IT3930.apartment.dto.TaskDTO> tasks = taskService.getAllTasks()
                    .stream()
                    .map(com.IT3930.apartment.dto.TaskDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(@RequestBody com.IT3930.apartment.dto.TaskDTO taskDTO) {
        try {
            com.IT3930.apartment.model.Task task = new com.IT3930.apartment.model.Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setType(taskDTO.getType());
            task.setIsDone(taskDTO.getIsDone());
            com.IT3930.apartment.model.Task created = taskService.createTask(task, taskDTO.getStaffIds());
            return ResponseEntity.ok(new com.IT3930.apartment.dto.TaskDTO(created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody com.IT3930.apartment.dto.TaskDTO taskDTO) {
        try {
            com.IT3930.apartment.model.Task task = new com.IT3930.apartment.model.Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setType(taskDTO.getType());
            task.setIsDone(taskDTO.getIsDone());
            com.IT3930.apartment.model.Task updated = taskService.updateTask(id, task, taskDTO.getStaffIds());
            return ResponseEntity.ok(new com.IT3930.apartment.dto.TaskDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok("Task deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
