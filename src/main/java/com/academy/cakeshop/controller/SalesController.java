package com.academy.cakeshop.controller;


import com.academy.cakeshop.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/api/v1/sales")
public class SalesController {

    private final SalesService salesService;

    @PostMapping("/process-deliveries")
    public ResponseEntity<Void> processDeliveries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        salesService.processDeliveries(date);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/process-sales")
    public ResponseEntity<Void> processSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        salesService.processSales(date);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
