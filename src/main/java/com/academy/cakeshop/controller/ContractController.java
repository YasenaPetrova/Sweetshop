package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.ContractRequest;
import com.academy.cakeshop.dto.ContractResponse;
import com.academy.cakeshop.dto.PaymentResponse;
import com.academy.cakeshop.service.ContractService;
import com.academy.cakeshop.service.PaymentService;
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
@RequestMapping("/api/v1/contracts")
@Validated
public class ContractController {
    private final ContractService contractService;
    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('STORE', 'MALL', 'SUPPLIER', 'MANAGER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ContractResponse> getById(@NotNull(message = "Required field!")
                                                    @Min(value = 1, message = "No negative values allowed")
                                                    @RequestParam(name = "contractId")
                                                    Long id) {
        ContractResponse contractResponse = contractService.getByID(id);
        return new ResponseEntity<>(contractResponse, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('STORE', 'ADMIN')")
    public String create(Principal principal, @RequestBody ContractRequest contractRequest) {
        String userName = principal.getName();
        contractService.create(contractRequest, userName);
        return "Contract successfully created!\nДоговорът беше успешно създаден!";
    }

    @PatchMapping(value = "/{contractId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public String updateContractStatus(@RequestBody ContractRequest contractRequest,
                                                  @NotNull(message = "Required field!")
                                                  @Min(value = 1, message = "No negative values allowed!")
                                                  @PathVariable Long contractId) {
        int updatedRows = contractService.updateContractStatus(contractRequest.contractStatus(), contractId);
        if (updatedRows > 0) {
            return "Contract status successfully changed!\nСтатусът беше променен успешно!";
        } else {
            return "Opps, something went wrong!\nВъзникна проблем!";
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@PathVariable @NotNull(message = "Required field!")
                                        @Min(value = 1, message = "No negative values allowed!") Long id) {
        contractService.deleteById(id);
        return "Contract successfully deleted!\nДоговорът беше изтринт успешно!";
    }

    @GetMapping("/payments/{contractId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE')")
    public ResponseEntity<List<PaymentResponse>> getAllPaymentsByContractId(@NotNull(message = "Required field!")
                                                                            @Min(value = 1, message = "No negative values allowed!")
                                                                            @PathVariable Long contractId) {
        List<PaymentResponse> paymentResponseList = paymentService.getAllByContractID(contractId);
        return new ResponseEntity<>(paymentResponseList, HttpStatus.OK);

    }
}