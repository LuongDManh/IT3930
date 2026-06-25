package com.IT3930.apartment.controller;

import com.IT3930.apartment.dto.AccountResponseDTO;
import com.IT3930.apartment.dto.AccountUpdateDTO;
import com.IT3930.apartment.model.account.Account;
import com.IT3930.apartment.security.CustomUserDetails;
import com.IT3930.apartment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Any authenticated user can update their own account details
    @PutMapping("/me")
    public ResponseEntity<?> updateMyAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AccountUpdateDTO updateDTO) {
        try {
            Account updatedAccount = accountService.updateOwnAccountDetails(
                    userDetails.getAccount(),
                    updateDTO.getEmail(),
                    updateDTO.getPassword(),
                    updateDTO.getPhone()
            );
            return ResponseEntity.ok(new AccountResponseDTO(updatedAccount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
