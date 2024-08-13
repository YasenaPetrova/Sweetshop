package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.dto.SaleResponseDTO;
import com.academy.cakeshop.persistance.entity.Product;
import com.academy.cakeshop.persistance.entity.RecipeProduct;
import com.academy.cakeshop.persistance.entity.Storage;
import com.academy.cakeshop.persistance.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SalesService {

    private static final Logger log = LoggerFactory.getLogger(SalesService.class);

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SaleRepository saleRepository;
    private final StorageRepository storageRepository;
    private final RecipeRepository recipeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void processDeliveries(LocalDate date) {
        List<PurchaseOrderRequestDTO> purchaseOrders = purchaseOrderRepository.findByOrderDate(date);
        for (PurchaseOrderRequestDTO order : purchaseOrders) {
            Optional<Storage> storageOpt = storageRepository.findByProductId(order.productId());
            if (storageOpt.isPresent()) {
                Storage storage = storageOpt.get();
                storage.setQuantity(storage.getQuantity() + order.quantity().doubleValue());
                storageRepository.save(storage);
                log.info("Updated storage for product ID {}: new quantity {}", order.productId(), storage.getQuantity());
            } else {
                Optional<Product> productOpt = productRepository.findById(order.productId());
                if (productOpt.isPresent()) {
                    Storage storage = new Storage();
                    storage.setProduct(productOpt.get());
                    storage.setQuantity(order.quantity().doubleValue());
                    storageRepository.save(storage);
                    log.info("Created new storage for product ID {}: quantity {}", order.productId(), storage.getQuantity());
                } else {
                    log.error("Product with ID {} not found", order.productId());
                    throw new RuntimeException("Product not found");
                }
            }
        }
    }

    public void processSales(LocalDate date) {
        List<SaleResponseDTO> sales = saleRepository.findBySaleDate(date);
        for (SaleResponseDTO sale : sales) {
            Optional<RecipeProduct> recipeOpt = recipeRepository.findByArticleId(sale.articleId());
            if (recipeOpt.isPresent()) {
                RecipeProduct recipe = recipeOpt.get();
                Product product = recipe.getProduct();
                Optional<Storage> storageOpt = storageRepository.findByProductId(product.getId());
                if (storageOpt.isPresent()) {
                    Storage storage = storageOpt.get();
                    double quantityToDeduct = recipe.getQuantity() * sale.amount();
                    if (storage.getQuantity() >= quantityToDeduct) {
                        storage.setQuantity(storage.getQuantity() - quantityToDeduct);
                        storageRepository.save(storage);
                        log.info("Updated storage for product ID {}: new quantity {}", product.getId(), storage.getQuantity());
                    } else {
                        log.error("Insufficient quantity for product ID {}: available {}, required {}", product.getId(), storage.getQuantity(), quantityToDeduct);
                        throw new RuntimeException("Insufficient quantity in storage");
                    }
                } else {
                    log.error("Product with ID {} not found in storage", product.getId());
                    throw new RuntimeException("Product not found in storage");
                }
            } else {
                log.error("Recipe not found for article ID {}", sale.articleId());
                throw new RuntimeException("Recipe not found for article");
            }
        }
    }
}