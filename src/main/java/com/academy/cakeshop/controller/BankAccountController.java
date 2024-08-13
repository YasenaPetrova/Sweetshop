package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.BankAccountRequest;
import com.academy.cakeshop.dto.BankAccountResponse;
import com.academy.cakeshop.service.BankAccountService;
import com.academy.cakeshop.validation.IBAN;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bankаccounts")
@Validated
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BankAccountResponse>> getAll() {
        List<BankAccountResponse> bankAccountResponseList = bankAccountService.getAll();
        return new ResponseEntity<>(bankAccountResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STORE', 'MALL', 'SUPPLIER', 'MANAGER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<BankAccountResponse> getById(@NotNull(message = "Required field!")
                                                       @Min(value = 1, message = "No negative values allowed")
                                                       @PathVariable
                                                       Long id) {
        BankAccountResponse bankAccountResponse = bankAccountService.getByID(id);
        return new ResponseEntity<>(bankAccountResponse, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public String create(Principal principal, @RequestBody BankAccountRequest bankAccountRequest) {
        bankAccountService.create(bankAccountRequest, principal.getName());
        return "Account successfully created!\nСметката беше създадена успешно!";
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@NotNull(message = "Required field!")
                                        @IBAN
                                        @RequestParam String iban) {
        bankAccountService.deleteByID(iban);
        return "Account successfully deleted!\nСметката беше изтрита успешно!";
    }
}