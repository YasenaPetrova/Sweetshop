package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.AccountHistoryRequestDTO;
import com.academy.cakeshop.persistance.entity.AccountHistory;
import com.academy.cakeshop.service.AccountHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account-history")
public class AccountHistoryController {

    private AccountHistoryService accountHistoryService;

    @PostMapping
    public ResponseEntity<?> createAccountHistory(@Valid @RequestBody AccountHistoryRequestDTO requestDTO) {
        AccountHistory accountHistory = accountHistoryService.createAccountHistory(requestDTO);
        return ResponseEntity.ok(accountHistory);
    }
    @GetMapping
    public ResponseEntity<List<AccountHistory>> getAllAccountHistories() {
        List<AccountHistory> accountHistories = accountHistoryService.getAllAccountHistories();
        return ResponseEntity.ok(accountHistories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountHistory> getAccountHistoryById(@PathVariable Long id) {
        Optional<AccountHistory> accountHistory = accountHistoryService.getAccountHistoryById(id);
        return accountHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountHistory> updateAccountHistory(@PathVariable Long id, @Valid @RequestBody AccountHistoryRequestDTO requestDTO) {
        AccountHistory updatedAccountHistory = accountHistoryService.updateAccountHistory(id, requestDTO);
        return ResponseEntity.ok(updatedAccountHistory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountHistory(@PathVariable Long id) {
        accountHistoryService.deleteAccountHistory(id);
        return ResponseEntity.noContent().build();
    }

}
