package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.StorageDTO;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.Storage;
import com.academy.cakeshop.service.StorageService;
import jakarta.validation.Valid;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/storages")
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<Storage> createStorage(@Valid @RequestBody Storage storage) {
        Storage createdStorage = storageService.createStorage(storage);
        return ResponseEntity.ok(createdStorage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Storage> getStorageById(@PathVariable Long id) {
        try {
            Storage storage = storageService.getStorageById(id);
            return ResponseEntity.ok(storage);
        } catch (BusinessNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Storage>> getAllStorages() {
        List<Storage> storages = storageService.getAllStorages();
        return ResponseEntity.ok(storages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Storage> updateStorage(@PathVariable Long id, @Valid @RequestBody StorageDTO storageDetails) {
        try {
            Storage updatedStorage = storageService.updateStorage(id, storageDetails);
            return ResponseEntity.ok(updatedStorage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }
}