package com.academy.cakeshop.service;

import com.academy.cakeshop.persistance.entity.Product;
import com.academy.cakeshop.persistance.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void createProduct() {
        Product product = new Product(5L, "Sugar", null);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Product createdProduct = productService.createProduct(product);
        assertEquals(product, createdProduct);
    }

    @Test
    void givenValidID_WhenGettingProductByID_ThenCorrectProductReturned() {
        Long id = 5L;
        Product expectedProduct = new Product(5L, "Sugar", null);
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(expectedProduct));
        Optional<Product> actualProduct = productService.getProductById(id);
        assertTrue(actualProduct.isPresent());
        assertEquals(expectedProduct, actualProduct.get(), "Products don't match");
    }

    @Test
    void givenInvalidID_WhenGettingProductByID_ThenThrowException() {
        Long id = 5L;
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(id));
        String expectedMessage = "Product not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getAllProducts() {
        Product expectedProduct1 = new Product(5L, "Sugar", null);
        Product expectedProduct2 = new Product(10L, "Flour", null);
        List<Product> expectedList = new ArrayList<>();
        expectedList.add(expectedProduct1);
        expectedList.add(expectedProduct2);

        Mockito.when(productRepository.findAll()).thenReturn(expectedList);
        List<Product> actualList = productService.getAllProducts();
        assertEquals(expectedList, actualList, "Lists don't match");
    }

    @Test
    void updateProduct() {
        Long id = 5L;
        Product existingProduct = new Product(id, "Sugar", null);
        Product updatedDetails = new Product(id, "Brown Sugar", null);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save(existingProduct)).thenReturn(updatedDetails);

        Product updatedProduct = productService.updateProduct(id, updatedDetails);
        assertEquals(updatedDetails.getName(), updatedProduct.getName());
    }

    @Test
    void givenInvalidID_WhenUpdatingProduct_ThenThrowException() {
        Long id = 5L;
        Product updatedDetails = new Product(id, "Brown Sugar", null);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(id, updatedDetails));
        String expectedMessage = "Product not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteProduct() {
        Long id = 5L;
        Mockito.doNothing().when(productRepository).deleteById(id);

        assertDoesNotThrow(() -> productService.deleteProduct(id));
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void givenInvalidID_WhenDeletingProduct_ThenThrowException() {
        Long id = 5L;
        Mockito.doThrow(new ResourceNotFoundException("Product not found")).when(productRepository).deleteById(id);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(id));
        String expectedMessage = "Product not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}

