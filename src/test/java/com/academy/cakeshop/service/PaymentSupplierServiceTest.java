package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.dto.SaleResponseDTO;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.persistance.entity.*;
import com.academy.cakeshop.persistance.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentSupplierServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;


    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private PaymentSupplierService paymentSupplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testProcessPayment() {
        // Mock today's date
        LocalDate today = LocalDate.now();

        // Mock purchase order data
        PurchaseOrderRequestDTO order = new PurchaseOrderRequestDTO(1, 50.0, today, "ACTIVE", 1L, 2L, "ACTIVE", 1L);

        // Mock contract data with a null currency (which causes the issue)
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setCurrency(null); // Simulate a null currency

        // Mock bank accounts
        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(1L);
        fromAccount.setIban("GB33BUKB20201555555555");

        BankAccount toAccount = new BankAccount();
        toAccount.setId(2L);
        toAccount.setIban("GB33BUKB20201555555555");
        toAccount.setBankAccountStatus(BankAccountStatus.ACTIVE);

        // Mock the behavior of repositories
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        when(bankAccountRepository.findByIbanEquals("GB33BUKB20201555555555")).thenReturn(fromAccount);
        when(bankAccountRepository.findByUserID(anyLong())).thenReturn(toAccount); // Mock this depending on your test scenario

        // Call the method under test
        assertThrows(NullPointerException.class, () -> paymentSupplierService.processPayment(order, 50.0));
    }
}