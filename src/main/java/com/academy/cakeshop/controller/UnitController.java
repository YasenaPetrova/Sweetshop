package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.UnitDTO;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.errorHandling.ResourceNotFoundException;
import com.academy.cakeshop.persistance.entity.Unit;
import com.academy.cakeshop.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/units")
public class UnitController {
    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping
    public ResponseEntity<Unit> createUnit(@Valid @RequestBody Unit unit) {
        Unit createdUnit = unitService.createUnit(unit);
        return ResponseEntity.ok(createdUnit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unit> getUnitById(@PathVariable Long id) {
        try {
            Unit unit = unitService.getUnitById(id);
            return ResponseEntity.ok(unit);
        } catch (BusinessNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Unit>> getAllUnits() {
        try {
            List<Unit> units = unitService.getAllUnits();
            return ResponseEntity.ok(units);
        } catch (BusinessNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unit> updateUnit(@PathVariable Long id, @Valid @RequestBody UnitDTO unitDetails) {
        try {
            Unit updatedUnit = unitService.updateUnit(id, unitDetails);
            return ResponseEntity.ok(updatedUnit);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }
}
