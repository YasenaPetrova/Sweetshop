package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.persistance.entity.PurchaseOrder;
import com.academy.cakeshop.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;


    @PostMapping
    public PurchaseOrder createPurchaseOrder(@RequestBody PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderRequestDTO);
        return purchaseOrderService.createPurchaseOrder(purchaseOrderRequestDTO);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrder();
        return ResponseEntity.ok(purchaseOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/purchase-orders/date")
    public List<PurchaseOrderRequestDTO> getPurchaseOrdersByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return purchaseOrderService.getPurchaseOrdersByDate(date);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@PathVariable Long id, @RequestBody PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        try {
            PurchaseOrder updatedPurchaseOrder = purchaseOrderService.updatePurchaseOrder(id, purchaseOrderRequestDTO);
            return ResponseEntity.ok(updatedPurchaseOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long id) {
        try {
            purchaseOrderService.deletePurchaseOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}