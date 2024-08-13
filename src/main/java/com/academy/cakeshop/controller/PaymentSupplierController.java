package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.service.PaymentSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/api/v1/payments_supplier")
@RequiredArgsConstructor
public class PaymentSupplierController {

    private final PaymentSupplierService paymentSupplierService;

    @PostMapping("/process-daily")
    @PreAuthorize("hasAnyRole('SUPPLIER','ADMIN')")
    public String processDailyPayments() {
        paymentSupplierService.processDailyPayments();
        return "Daily payment processing initiated.";
    }

    @PostMapping("/process-payment")
    public String processPayment(@RequestBody PurchaseOrderRequestDTO order) {
        paymentSupplierService.processPayment(order, Double.MAX_VALUE);
        return "Payment processed for order ID: " + order.contractId()
                + "\nОбработено плащане за: " + order.contractId();
    }

    @PostMapping("/advance")
    public ResponseEntity<String> processAdvancePayment(@RequestBody PurchaseOrderRequestDTO order) {
        try {
            paymentSupplierService.processAdvancePaymentRequest(order);
            return ResponseEntity.ok("Advance payment processed successfully for order ID: " + order.contractId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process advance payment: "
                    + e.getMessage());
        }
    }
}
