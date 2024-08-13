package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.SaleRequestDTO;
import com.academy.cakeshop.dto.SaleResponseDTO;
import com.academy.cakeshop.persistance.entity.Sale;
import com.academy.cakeshop.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sales")
public class SaleController {


    private final SaleService saleService;


    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody SaleRequestDTO saleRequestDTO) {
        Sale createdSale = saleService.createSale(saleRequestDTO);
        return ResponseEntity.ok(createdSale);
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.getAllSale();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/sales/date")
    public ResponseEntity<List<SaleResponseDTO>> getSalesByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SaleResponseDTO> sales = saleService.getSalesByDate(date);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody SaleRequestDTO saleRequestDTO) {
        try {
            Sale updatedSale = saleService.updateSale(id, saleRequestDTO);
            return ResponseEntity.ok(updatedSale);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        try {
            saleService.deleteSale(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}