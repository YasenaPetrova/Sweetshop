package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.PaymentResponse;
import com.academy.cakeshop.service.PaymentService;
import com.academy.cakeshop.service.RentService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {
    private final PaymentService paymentService;
    private final RentService rentService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE')")
    public ResponseEntity<PaymentResponse> getById(@NotBlank(message = "Required field!")
                                                       @Min(value = 1, message = "No negative values allowed!")
                                                       @PathVariable Long id) {
        PaymentResponse paymentResponse = paymentService.getByID(id);
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

    @PostMapping("/income")
    @PreAuthorize("hasRole='STORE'")
    public ResponseEntity<String> distributeIncome(
            @RequestParam("saleDate") LocalDate saleDate,
            @RequestParam("rentAmount") double rentAmount) {
        try {
            rentService.distributeIncome(saleDate, rentAmount);
            return ResponseEntity.ok("Income distributed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error distributing income: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePaymentById(@NotBlank(message = "Required field!")
                                                   @Min(value = 1, message = "No negative values allowed!")
                                                   @PathVariable Long id) {
        paymentService.deleteByID(id);
        return "Payment successfully deleted!\n Плащането беше успешно изтрито!";
    }
}