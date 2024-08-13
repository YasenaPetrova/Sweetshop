package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.RentDTO;
import com.academy.cakeshop.dto.SaleResponseDTO;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.errorHandling.InsufficientFundsException;
import com.academy.cakeshop.persistance.entity.AccountHistory;
import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.persistance.repository.AccountHistoryRepository;
import com.academy.cakeshop.persistance.repository.BankAccountRepository;
import com.academy.cakeshop.persistance.repository.SaleRepository;
import com.academy.cakeshop.enumeration.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private AccountHistoryRepository accountHistoryRepository;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private RentService rentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDistributeIncome_Success() {
        String storeAccountEURIBAN = "DE75512108001245126199";
        String storeAccountBGNIBAN = "GB33BUKB20201555555555";
        LocalDate saleDate = LocalDate.now();
        double rentAmount = 1000.0;

        BankAccount storeAccount1 = new BankAccount();
        storeAccount1.setIban(storeAccountEURIBAN);
        storeAccount1.setBankAccountStatus(BankAccountStatus.ACTIVE);
        storeAccount1.setBalance(0.0);
        storeAccount1.setCurrency(Currency.EUR);

        BankAccount storeAccount2 = new BankAccount();
        storeAccount2.setIban(storeAccountBGNIBAN);
        storeAccount2.setBankAccountStatus(BankAccountStatus.ACTIVE);
        storeAccount2.setBalance(0.0);
        storeAccount2.setCurrency(Currency.EUR);

        when(bankAccountRepository.findByIbanEquals(storeAccountEURIBAN)).thenReturn(storeAccount1);
        when(bankAccountRepository.findByIbanEquals(storeAccountBGNIBAN)).thenReturn(storeAccount2);

        SaleResponseDTO sale1 = new SaleResponseDTO(LocalDate.now(), 500.0, 1L);
        SaleResponseDTO sale2 = new SaleResponseDTO(LocalDate.now(), 500.0, 2L);

        List<SaleResponseDTO> salesDTO = List.of(sale1, sale2);
        when(saleRepository.findBySaleDate(saleDate)).thenReturn(salesDTO);

        rentService.distributeIncome(saleDate, rentAmount);

        assertEquals(300.0 * 1.96, storeAccount1.getBalance());
        assertEquals(700.0, storeAccount2.getBalance());
        verify(bankAccountRepository, times(1)).save(storeAccount1);
        verify(bankAccountRepository, times(1)).save(storeAccount2);
        verify(accountHistoryRepository, times(2)).save(any(AccountHistory.class));
    }

    @Test
    void distributeIncome_StoreAccountNotFound() {
        String storeAccountEURIBAN = "DE75512108001245126199";
        String storeAccountBGNIBAN = "GB33BUKB20201555555555";
        LocalDate saleDate = LocalDate.now();
        double rentAmount = 1000.0;

        when(bankAccountRepository.findByIbanEquals(storeAccountEURIBAN)).thenReturn(null);

        BusinessNotFound thrown = assertThrows(
                BusinessNotFound.class,
                () -> rentService.distributeIncome(saleDate, rentAmount),
                "Expected distributeIncome to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Store account 1 not found or inactive"));
    }

    @Test
    void testPayRent_Success() {
        String storeAccountEURIBAN = "DE75512108001245126199";
        Long mallAccountId = 2L;
        LocalDate dueDate = LocalDate.now().minusDays(1);
        double rentAmount = 500.0;

        RentDTO rentDTO = new RentDTO(1L, mallAccountId, rentAmount, dueDate);

        BankAccount storeAccount = new BankAccount();
        storeAccount.setIban(storeAccountEURIBAN);
        storeAccount.setBankAccountStatus(BankAccountStatus.ACTIVE);
        storeAccount.setBalance(1000.0);
        storeAccount.setCurrency(Currency.EUR);


        BankAccount mallAccount = new BankAccount();
        mallAccount.setId(mallAccountId);
        mallAccount.setBalance(0.0);
        mallAccount.setBankAccountStatus(BankAccountStatus.ACTIVE);
        mallAccount.setCurrency(Currency.EUR);

        when(bankAccountRepository.findByIbanEquals(storeAccountEURIBAN)).thenReturn(storeAccount);
        when(bankAccountRepository.getReferenceById(mallAccountId)).thenReturn(mallAccount);

        rentService.payRent(rentDTO);

        assertEquals(500.0, storeAccount.getBalance());
        assertEquals(500.0, mallAccount.getBalance());
        verify(bankAccountRepository, times(1)).save(storeAccount);
        verify(bankAccountRepository, times(1)).save(mallAccount);
        verify(accountHistoryRepository, times(2)).save(any(AccountHistory.class));
    }

    @Test
    void payRent_InsufficientFunds() {
        String storeAccountEURIBAN = "DE75512108001245126199";
        Long mallAccountId = 2L;
        LocalDate dueDate = LocalDate.now().minusDays(1);
        double rentAmount = 500.0;

        RentDTO rentDTO = new RentDTO(1L, mallAccountId, rentAmount, dueDate);

        BankAccount storeAccount = new BankAccount();
        storeAccount.setIban(storeAccountEURIBAN);
        storeAccount.setBalance(100.0);
        storeAccount.setBankAccountStatus(BankAccountStatus.ACTIVE);

        BankAccount mallAccount = new BankAccount();
        mallAccount.setId(mallAccountId);
        mallAccount.setBalance(0.0);
        mallAccount.setBankAccountStatus(BankAccountStatus.ACTIVE);

        when(bankAccountRepository.findByIbanEquals(storeAccountEURIBAN)).thenReturn(storeAccount);
        when(bankAccountRepository.getReferenceById(mallAccountId)).thenReturn(mallAccount);

        InsufficientFundsException thrown = assertThrows(
                InsufficientFundsException.class,
                () -> rentService.payRent(rentDTO),
                "Expected payRent to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Недостатъчни средства или плащане преди падежа"));
    }
}
