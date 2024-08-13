package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.ProductDTO;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.Product;
import com.academy.cakeshop.persistance.entity.Storage;
import com.academy.cakeshop.service.ProductService;
import com.academy.cakeshop.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final RentService rentService;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductByID(id);
            return ResponseEntity.ok(product);
        } catch (BusinessNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity <List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
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

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
