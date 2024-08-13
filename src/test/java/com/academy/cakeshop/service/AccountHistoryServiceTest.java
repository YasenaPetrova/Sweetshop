package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.AccountHistoryRequestDTO;
import com.academy.cakeshop.persistance.entity.AccountHistory;
import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.persistance.repository.AccountHistoryRepository;
import com.academy.cakeshop.persistance.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountHistoryServiceTest {

    @Mock
    private AccountHistoryRepository accountHistoryRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private AccountHistoryService accountHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccountHistory_Success() {
        AccountHistoryRequestDTO requestDTO = new AccountHistoryRequestDTO(
                LocalDate.now(), "BG1234567890", "BG0987654321", 100.0, "EUR");

        BankAccount fromAccount = new BankAccount();
        fromAccount.setIban("BG1234567890");

        BankAccount toAccount = new BankAccount();
        toAccount.setIban("BG0987654321");

        when(bankAccountRepository.findByIbanEquals("BG1234567890")).thenReturn(fromAccount);
        when(bankAccountRepository.findByIbanEquals("BG0987654321")).thenReturn(toAccount);
        when(accountHistoryRepository.save(any(AccountHistory.class))).thenReturn(new AccountHistory());

        AccountHistory accountHistory = accountHistoryService.createAccountHistory(requestDTO);

        assertNotNull(accountHistory);
        verify(bankAccountRepository, times(1)).findByIbanEquals("BG1234567890");
        verify(bankAccountRepository, times(1)).findByIbanEquals("BG0987654321");
        verify(accountHistoryRepository, times(1)).save(any(AccountHistory.class));
    }

    @Test
    void testGetAllAccountHistories() {
        List<AccountHistory> accountHistoryList = List.of(new AccountHistory());

        when(accountHistoryRepository.findAll()).thenReturn(accountHistoryList);

        List<AccountHistory> result = accountHistoryService.getAllAccountHistories();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accountHistoryRepository, times(1)).findAll();
    }

    @Test
    void testGetAccountHistoryById() {
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setId(1L);

        when(accountHistoryRepository.findById(1L)).thenReturn(Optional.of(accountHistory));

        Optional<AccountHistory> result = accountHistoryService.getAccountHistoryById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(accountHistoryRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateAccountHistory_Success() {
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setId(1L);

        AccountHistoryRequestDTO requestDTO = new AccountHistoryRequestDTO(
                LocalDate.now(), "BG1234567890", "BG0987654321", 200.0, "USD");

        BankAccount fromAccount = new BankAccount();
        fromAccount.setIban("BG1234567890");

        BankAccount toAccount = new BankAccount();
        toAccount.setIban("BG0987654321");

        when(accountHistoryRepository.findById(1L)).thenReturn(Optional.of(accountHistory));
        when(bankAccountRepository.findByIbanEquals("BG1234567890")).thenReturn(fromAccount);
        when(bankAccountRepository.findByIbanEquals("BG0987654321")).thenReturn(toAccount);
        when(accountHistoryRepository.save(any(AccountHistory.class))).thenReturn(new AccountHistory());

        AccountHistory updatedAccountHistory = accountHistoryService.updateAccountHistory(1L, requestDTO);

        assertNotNull(updatedAccountHistory);
        verify(accountHistoryRepository, times(1)).findById(1L);
        verify(bankAccountRepository, times(1)).findByIbanEquals("BG1234567890");
        verify(bankAccountRepository, times(1)).findByIbanEquals("BG0987654321");
        verify(accountHistoryRepository, times(1)).save(any(AccountHistory.class));
    }



    @Test
    void testDeleteAccountHistory_Success() {
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setId(1L);

        when(accountHistoryRepository.findById(1L)).thenReturn(Optional.of(accountHistory));

        accountHistoryService.deleteAccountHistory(1L);

        verify(accountHistoryRepository, times(1)).findById(1L);
        verify(accountHistoryRepository, times(1)).deleteById(1L);
    }


}
