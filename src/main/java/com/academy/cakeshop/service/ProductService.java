package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.ProductDTO;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.Product;
import com.academy.cakeshop.persistance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductByID (Long id) {
        Product product = productRepository.getReferenceById(id);
        if (product != null){
            return product;
        }else {
            throw new BusinessNotFound("Product ID does not exist!");
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, ProductDTO productDetails) {
        Product product = productRepository.getReferenceById(id);
        if (product != null) {
            product.setPricePerUnit(productDetails.pricePerUnit());
            return productRepository.save(product);
        }else {
            throw new BusinessNotFound("No Product with: " + id + " Found!");
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
