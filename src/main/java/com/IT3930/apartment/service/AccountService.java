package com.IT3930.apartment.service;

import com.IT3930.apartment.model.Role;
import com.IT3930.apartment.model.account.Account;
import com.IT3930.apartment.model.account.Admin;
import com.IT3930.apartment.model.account.Owner;
import com.IT3930.apartment.model.account.Staff;
import com.IT3930.apartment.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;
import com.IT3930.apartment.repository.ApartmentRepository;
import com.IT3930.apartment.model.Apartment;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Account createAccount(String email, String password, String phone, Role role) {

        if (accountRepository.findByEmail(email).isPresent()) {
            throw new AccountAlreadyExistsException("Email already in use.");
        }

        Account newAccount;
        switch (role) {
            case ADMIN:
                newAccount = new Admin();
                break;
            case STAFF:
                newAccount = new Staff();
                break;
            case OWNER:
                newAccount = new Owner();
                break;
            default:
                throw new InvalidRoleException("Invalid role: " + role);
        }

        String token = java.util.UUID.randomUUID().toString();
        newAccount.setVerificationToken(token);
        newAccount.setEmail(email);
        newAccount.setPassword(passwordEncoder.encode(password));
        newAccount.setPhone(phone);
        newAccount.setActive(false); // isActive default is false
        // role is already set by the constructors of Admin/Staff/Owner, so it cannot be changed later

        Account savedAccount = accountRepository.save(newAccount);
        
        emailService.sendVerificationEmail(savedAccount.getEmail(), token);
        
        return savedAccount;
    }

    @Transactional
    public boolean verifyAccount(String token) {
        Optional<Account> accountOpt = accountRepository.findByVerificationToken(token);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setActive(true);
            account.setVerificationToken(null);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Transactional
    public Account updateOwnAccountDetails(Account currentAccount, String newEmail, String newPassword, String newPhone) {

        if (newEmail != null && !newEmail.isEmpty()) {
            Optional<Account> existing = accountRepository.findByEmail(newEmail);
            if (existing.isPresent() && !existing.get().getId().equals(currentAccount.getId())) {
                throw new AccountAlreadyExistsException("Email already in use.");
            }
            currentAccount.setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            currentAccount.setPassword(passwordEncoder.encode(newPassword));
        }

        if (newPhone != null && !newPhone.isEmpty()) {
            currentAccount.setPhone(newPhone);
        }

        return accountRepository.save(currentAccount);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account updateAccountByAdmin(Long accountId, String newEmail, String newPassword, String newPhone, Boolean isActive) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        if (newEmail != null && !newEmail.isEmpty()) {
            Optional<Account> existing = accountRepository.findByEmail(newEmail);
            if (existing.isPresent() && !existing.get().getId().equals(account.getId())) {
                throw new AccountAlreadyExistsException("Email already in use.");
            }
            account.setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            account.setPassword(passwordEncoder.encode(newPassword));
        }

        if (newPhone != null && !newPhone.isEmpty()) {
            account.setPhone(newPhone);
        }

        if (isActive != null) {
            account.setActive(isActive);
        }

        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));
        
        // If owner, unassign apartments first
        if (account instanceof Owner) {
            List<Apartment> assigned = apartmentRepository.findByOwnerIdOrderByIdAsc(accountId);
            for (Apartment apt : assigned) {
                apt.setOwner(null);
                apartmentRepository.save(apt);
            }
        }
        
        accountRepository.delete(account);
    }

    public List<Apartment> getAvailableApartments() {
        return apartmentRepository.findByOwnerIsNullOrderByIdAsc();
    }

    public List<Apartment> getApartmentsByOwner(Long ownerId) {
        return apartmentRepository.findByOwnerIdOrderByIdAsc(ownerId);
    }

    @Transactional
    public void assignApartmentsToOwner(Long ownerId, List<Long> apartmentIds) {
        Account account = accountRepository.findById(ownerId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        if (!(account instanceof Owner)) {
            throw new IllegalArgumentException("Account is not an Owner.");
        }
        Owner owner = (Owner) account;

        // Unassign all existing apartments for this owner
        List<Apartment> currentlyAssigned = apartmentRepository.findByOwnerIdOrderByIdAsc(ownerId);
        for (Apartment apt : currentlyAssigned) {
            apt.setOwner(null);
            apartmentRepository.save(apt);
        }

        // Assign the new ones
        if (apartmentIds != null) {
            for (Long aptId : apartmentIds) {
                Apartment apt = apartmentRepository.findById(aptId).orElse(null);
                if (apt != null) {
                    apt.setOwner(owner);
                    apartmentRepository.save(apt);
                }
            }
        }
    }
}
