package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.persistance.entity.*;
import com.academy.cakeshop.persistance.repository.ContractRepository;
import com.academy.cakeshop.persistance.repository.ProductRepository;
import com.academy.cakeshop.persistance.repository.PurchaseOrderRepository;
import com.academy.cakeshop.persistance.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePurchaseOrder_Success() {
        PurchaseOrderRequestDTO purchaseOrderRequestDTO = new PurchaseOrderRequestDTO(
                10, 100.0, LocalDate.now(), "ACTIVE", 1L, 1L, "ACTIVE", 1L);

        Product product = new Product();
        product.setId(1L);

        Unit unit = new Unit();
        unit.setId(1L);

        Contract contract = new Contract();
        contract.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(new PurchaseOrder());

        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderRequestDTO);

        assertNotNull(purchaseOrder);
        verify(productRepository, times(1)).findById(1L);
        verify(unitRepository, times(1)).findById(1L);
        verify(contractRepository, times(1)).findById(1L);
        verify(purchaseOrderRepository, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    void testCreatePurchaseOrder_ProductNotFound() {
        PurchaseOrderRequestDTO purchaseOrderRequestDTO = new PurchaseOrderRequestDTO(
                10, 100.0, LocalDate.now(), "ACTIVE", 1L, 1L, "ACTIVE", 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            purchaseOrderService.createPurchaseOrder(purchaseOrderRequestDTO);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(unitRepository, times(0)).findById(anyLong());
        verify(contractRepository, times(0)).findById(anyLong());
        verify(purchaseOrderRepository, times(0)).save(any(PurchaseOrder.class));
    }

    @Test
    void testGetAllPurchaseOrder() {
        List<PurchaseOrder> purchaseOrderList = List.of(new PurchaseOrder());

        when(purchaseOrderRepository.findAll()).thenReturn(purchaseOrderList);

        List<PurchaseOrder> result = purchaseOrderService.getAllPurchaseOrder();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(purchaseOrderRepository, times(1)).findAll();
    }

    @Test
    void testGetPurchaseOrderById() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);

        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));

        Optional<PurchaseOrder> result = purchaseOrderService.getPurchaseOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(purchaseOrderRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdatePurchaseOrder_Success() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);

        PurchaseOrderRequestDTO purchaseOrderRequestDTO = new PurchaseOrderRequestDTO(
                10, 100.0, LocalDate.now(), "ACTIVE", 1L, 1L, "ACTIVE", 1L);

        Product product = new Product();
        product.setId(1L);

        Unit unit = new Unit();
        unit.setId(1L);

        Contract contract = new Contract();
        contract.setId(1L);

        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(new PurchaseOrder());

        PurchaseOrder updatedPurchaseOrder = purchaseOrderService.updatePurchaseOrder(1L, purchaseOrderRequestDTO);

        assertNotNull(updatedPurchaseOrder);
        verify(purchaseOrderRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(unitRepository, times(1)).findById(1L);
        verify(contractRepository, times(1)).findById(1L);
        verify(purchaseOrderRepository, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    void testDeletePurchaseOrder_Success() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);

        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));

        purchaseOrderService.deletePurchaseOrder(1L);

        verify(purchaseOrderRepository, times(1)).findById(1L);
        verify(purchaseOrderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePurchaseOrder_NotFound() {
        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            purchaseOrderService.deletePurchaseOrder(1L);
        });

        assertEquals("Purchase order not found", exception.getMessage());
        verify(purchaseOrderRepository, times(1)).findById(1L);
        verify(purchaseOrderRepository, times(0)).deleteById(anyLong());
    }
}
