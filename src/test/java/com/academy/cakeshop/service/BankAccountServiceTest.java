package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.BankAccountRequest;
import com.academy.cakeshop.dto.BankAccountRequestCurrencyChange;
import com.academy.cakeshop.dto.BankAccountResponse;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.enumeration.Currency;
import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.errorHandling.FailedMoneyTransaction;
import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.BankAccountRepository;
import com.academy.cakeshop.persistance.repository.UserRepository;
import com.academy.cakeshop.mail.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class BankAccountServiceTest {
    BankAccountService bankAccountService;
    BankAccountRepository bankAccountRepository;
    UserRepository userRepository;
    EmailService emailService;

    @BeforeEach
    void setUp() {
        bankAccountRepository = Mockito.mock(BankAccountRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        emailService = Mockito.mock(EmailService.class);
        bankAccountService = new BankAccountService(bankAccountRepository, userRepository, emailService);
    }

    @Test
    void givenValidId_whenGettingBankAccountById_thenReturnCorrectBankAccount() {
        Long id = 15L;
        User user = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount = new BankAccount(id, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        String owner = bankAccount.getUser().getFirstName() + " " + bankAccount.getUser().getLastName();
        BankAccountResponse expectedBankAccountResponse = new BankAccountResponse(bankAccount.getIban(),
                bankAccount.getCurrency().toString(),
                bankAccount.getBalance(), owner, bankAccount.getBankAccountStatus().toString());

        Mockito.when(bankAccountRepository.getReferenceById(id)).thenReturn(bankAccount);
        BankAccountResponse actualBankAccountResponse = bankAccountService.getByID(id);

        assertEquals(expectedBankAccountResponse, actualBankAccountResponse);
    }

    @Test
    void givenInvalidId_whenGettingBankAccountById_thenExceptionThrown() {
        Long id = 10L;
        Mockito.when(bankAccountRepository.getReferenceById(id)).thenReturn(null);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> bankAccountService.getByID(id));
        String expectedMessage = "BankAccount with id: " + id + "does not exist!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenGettingBankAccountByUserId_thenReturnCorrectBankAccount() {
        Long id = 15L;
        Long userId = 1L;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount = new BankAccount(id, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        String owner = bankAccount.getUser().getFirstName() + " " + bankAccount.getUser().getLastName();
        BankAccountResponse expectedBankAccountResponse = new BankAccountResponse(bankAccount.getIban(),
                bankAccount.getCurrency().toString(),
                bankAccount.getBalance(), owner, bankAccount.getBankAccountStatus().toString());

        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(bankAccount);
        BankAccountResponse actualBankAccountResponse = bankAccountService.getByUserID(userId);

        assertEquals(expectedBankAccountResponse, actualBankAccountResponse);
    }

    @Test
    void givenInvalidId_whenGettingBankAccountByUserId_thenExceptionThrown() {
        Long userId = 1L;

        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> bankAccountService.getByUserID(userId));
        String expectedMessage = "BankAccount with userID: " + userId + " does not exist!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenGettingAllBankAccounts_thenReturnAListOfThem() {
        User user1 = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        User user2 = new User(2L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount1 = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user1);
        BankAccount bankAccount2 = new BankAccount(2L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user2);

        String owner1 = bankAccount1.getUser().getFirstName() + " " + bankAccount1.getUser().getLastName();
        BankAccountResponse expectedBankAccountResponse1 = new BankAccountResponse(bankAccount1.getIban(),
                bankAccount1.getCurrency().toString(),
                bankAccount1.getBalance(), owner1, bankAccount1.getBankAccountStatus().toString());
        String owner2 = bankAccount2.getUser().getFirstName() + " " + bankAccount2.getUser().getLastName();
        BankAccountResponse expectedBankAccountResponse2 = new BankAccountResponse(bankAccount2.getIban(),
                bankAccount2.getCurrency().toString(),
                bankAccount2.getBalance(), owner2, bankAccount2.getBankAccountStatus().toString());

        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount1);
        bankAccountList.add(bankAccount2);

        Mockito.when(bankAccountRepository.findAll()).thenReturn(bankAccountList);

        List<BankAccountResponse> expectedBankAccountResponseList = new ArrayList<>();
        expectedBankAccountResponseList.add(expectedBankAccountResponse1);
        expectedBankAccountResponseList.add(expectedBankAccountResponse2);

        List<BankAccountResponse> actualBankAccountResponseList = bankAccountService.getAll();

        assertEquals(expectedBankAccountResponseList, actualBankAccountResponseList);
    }

//    TODO Ask Nadeto
    @Test
    void givenInvalidId_whenGettingAllBankAccounts_thenExceptionThrown() {
        Mockito.when(bankAccountRepository.findAll()).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> bankAccountService.getAll());
        String expectedMessage = "No bank accounts found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidIBAN_whenCheckingIfBankAccountExistsByIBAN_thenReturnTrue() {
        String IBAN = "GB33BUKB20201555555555";
        User user = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(bankAccount);
        assertTrue(bankAccountService.existsByIBAN(IBAN));
    }

    @Test
    void givenInvalidIBAN_whenCheckingIfBankAccountExistsByIBAN_thenReturnFalse() {
        String IBAN = "GB33BUKB20201555555555";
        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(null);
        assertFalse(bankAccountService.existsByIBAN(IBAN));
    }

    @Test
    void create() {
        BankAccountRequest bankAccountRequest = new BankAccountRequest("GB33BUKB20201555555555",
                100.0, "BGN");
        String userName = "hrisiA";
        User user = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(user);
        Mockito.when(bankAccountRepository.saveAndFlush(any())).thenReturn(new BankAccount());

        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban(bankAccountRequest.iban());
        bankAccount.setBalance(bankAccountRequest.balance());
        bankAccount.setCurrency(Currency.getCurrencyFromString(bankAccountRequest.currency()));
        bankAccount.setBankAccountStatus(BankAccountStatus.ACTIVE);
        bankAccount.setUser(user);

        bankAccountService.create(bankAccountRequest, userName);
        Mockito.verify(bankAccountRepository).saveAndFlush(any());
    }

    @Test
    void givenValidUserId_whenUpdatingBankAccountCurrency_thenReturnUpdatedRowsCount() {
        Currency currency = Currency.EUR;
        Long userId = 1L;
        int expectedUpdatedRows = 1;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(bankAccount);
        Mockito.when(bankAccountRepository.updateCurrencyBy(currency, userId)).thenReturn(expectedUpdatedRows);

        int actualUpdatedRows = bankAccountService.updateBankAccountCurrency(currency, userId);
        assertEquals(expectedUpdatedRows, actualUpdatedRows);
    }

    @Test
    void givenInvalidUserId_whenUpdatingBankAccountCurrency_thenExceptionThrown() {
        Currency currency = Currency.EUR;
        Long userId = 1L;
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                bankAccountService.updateBankAccountCurrency(currency, userId));
        String expectedMessage = "BankAccount with userID: " + userId + "does not exist!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidUserId_whenUpdatingBankAccountBalance_thenReturnUpdatedRowsCount() {
        double balance = 200.0;
        Long userId = 1L;
        int expectedUpdatedRows = 1;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(bankAccount);
        Mockito.when(bankAccountRepository.updateBalanceBy(balance, userId)).thenReturn(expectedUpdatedRows);

        int actualUpdatedRows = bankAccountService.updateBankAccountBalance(balance, userId);
        assertEquals(expectedUpdatedRows, actualUpdatedRows);
    }

    @Test
    void givenInvalidUserId_whenUpdatingBankAccountBalance_thenExceptionThrown() {
        double balance = 200.0;
        Long userId = 1L;
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                bankAccountService.updateBankAccountBalance(balance, userId));
        String expectedMessage = "BankAccount with userID: " + userId + "does not exist!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteByID() {
        String IBAN = "GB33BUKB20201555555555";
        User user = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(bankAccount);
        bankAccountService.deleteByID(IBAN);
        Mockito.verify(bankAccountRepository, Mockito.times(1)).deleteById(bankAccount.getId());
    }

    @Test
    void givenValidIban_whenGettingBalanceByIban_thenReturnCorrectBalance() {
        String IBAN = "GB33BUKB20201555555555";
        Long userId = 1L;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount bankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(bankAccount);
        double expectedBalance = bankAccount.getBalance();
        double actualBalance = bankAccountService.getBalanceByIBAN(IBAN);
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void givenInvalidIban_whenGettingBalanceByIban_thenExceptionThrown() {
        String IBAN = "GB33BUKB20201555555555";
        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                bankAccountService.getBalanceByIBAN(IBAN));
        String expectedMessage = "BankAccount with iban: " + IBAN + "does not exist!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenInvalidIBAN_whenDepositingMoney_thenExceptionThrown() {
        String IBAN = "GB33BUKB20201555555555";
        Long userId = 1L;
        double sumToDeposit = 100.0;
        int updatedRows = 5;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount expectedBankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);

        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                bankAccountService.deposit(sumToDeposit, IBAN));
        String expectedMessage = "BankAccount with iban: " + IBAN + "does not exist!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidIBAN_whenDepositingMoney_thenReturnUpdatedBankAccount() {
        String IBAN = "GB33BUKB20201555555555";
        Long userId = 1L;
        double sumToDeposit = 100.0;
        int updatedRows = 5;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount expectedBankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);

        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.updateBalanceBy(sumToDeposit, userId)).thenReturn(updatedRows);
        expectedBankAccount.setBalance(expectedBankAccount.getBalance() + sumToDeposit);

        BankAccount actualBankAccount = bankAccountService.deposit(sumToDeposit, IBAN);
        assertEquals(expectedBankAccount, actualBankAccount);
    }

    @Test
    void givenValidIBAN_whenWithdrawingMoney_thenExceptionThrown() {
        String IBAN = "GB33BUKB20201555555555";
        Long userId = 1L;
        double sumToWithdraw = 50.0;
        int updatedRows = 5;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount expectedBankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);

        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.updateBalanceBy(sumToWithdraw, userId)).thenReturn(updatedRows);
        expectedBankAccount.setBalance(expectedBankAccount.getBalance() + sumToWithdraw);

        BankAccount actualBankAccount = bankAccountService.withdraw(sumToWithdraw, IBAN);
        assertEquals(expectedBankAccount, actualBankAccount);
    }

    @Test
    void givenInvalidIBAN_whenWithdrawingMoney_thenBusinessNotFoundExceptionThrown() {
        String IBAN = "GB33BUKB20201555555555";
        Long userId = 1L;
        double sumToWithdraw = 150.0;
        int updatedRows = 5;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount expectedBankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);

        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.updateBalanceBy(sumToWithdraw, userId)).thenReturn(updatedRows);

        FailedMoneyTransaction exception = assertThrows(FailedMoneyTransaction.class, () ->
                bankAccountService.withdraw(sumToWithdraw, IBAN));
        String expectedMessage = "Unable to complete transaction due to low account balance!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenIncorrectSum_whenWithdrawingMoney_thenFailedMoneyTransactionExceptionThrown() {
        String IBAN = "GB33BUKB20201555555555";
        Long userId = 1L;
        double sumToWithdraw = 50.0;
        int updatedRows = 5;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        BankAccount expectedBankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);

        Mockito.when(bankAccountRepository.findByIbanEquals(IBAN)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                bankAccountService.withdraw(sumToWithdraw, IBAN));
        String expectedMessage = "BankAccount with iban: " + IBAN + "does not exist!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidInput_whenUpdatingBankAccount_thenUpdateAccountCurrencyAndBalance() {
        Long userId = 3L;
        String iban = "GB33BUKB20201555555555";
        int expectedUpdatedRows = 1;
        String userName = "hrisiA";
        BankAccountRequestCurrencyChange bankAccountRequestCurrencyChange = new BankAccountRequestCurrencyChange(
                "GB33BUKB20201555555555", 15000.0, "BGN", "EUR");

        Currency currency = Currency.getCurrencyFromString(bankAccountRequestCurrencyChange.fromCurrency());
        User user = new User(userId, "Christina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.MALL);
        BankAccount expectedBankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
                bankAccountRequestCurrencyChange.balance(), currency, BankAccountStatus.ACTIVE, user);

        double balance = bankAccountRequestCurrencyChange.balance() * 0.51;
        Currency toCurrency = Currency.getCurrencyFromString(bankAccountRequestCurrencyChange.toCurrency());
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(user);
        Mockito.when(bankAccountRepository.findByIbanEquals(iban)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(expectedBankAccount);
        Mockito.when(bankAccountRepository.updateBalanceBy(balance, userId)).thenReturn(expectedUpdatedRows);
        Mockito.when(bankAccountRepository.updateCurrencyBy(toCurrency, userId)).thenReturn(expectedUpdatedRows);

        int actualUpdatedRows = bankAccountService.updateBankAccount(bankAccountRequestCurrencyChange, userName);

        assertEquals(expectedUpdatedRows * 2, actualUpdatedRows);
    }

    //TODO
    @Test
    void givenValidInput_whenUpdatingBankAccount_thenExceptionThrown() {
        Long userId = 3L;
        String iban = "GB33BUKB20201555555555";
        int expectedUpdatedRows = 1;
        String userName = "hrisiA";
        BankAccountRequestCurrencyChange bankAccountRequestCurrencyChange = new BankAccountRequestCurrencyChange(
                "GB33BUKB20201555555555", 15000.0, "", "");

//        Currency currency = Currency.getCurrencyFromString(bankAccountRequestCurrencyChange.fromCurrency());
//        User user = new User(userId, "Christina", "Ablanska", "hrisiA", "1234",
//                "hr.abl@gmail.com", "0888524163", "test", Role.MALL);
//        BankAccount expectedBankAccount = new BankAccount(1L, "GB33BUKB20201555555555",
//                bankAccountRequestCurrencyChange.balance(), currency, BankAccountStatus.ACTIVE, user);
//
//        double balance = bankAccountRequestCurrencyChange.balance() * 0.51;
//        Currency toCurrency = Currency.getCurrencyFromString(bankAccountRequestCurrencyChange.toCurrency());
//        Mockito.when(bankAccountRepository.findByIbanEquals(iban)).thenReturn(expectedBankAccount);
//        Mockito.when(bankAccountRepository.findByUserID(userId)).thenReturn(expectedBankAccount);
//        Mockito.when(bankAccountRepository.updateBalanceBy(balance, userId)).thenReturn(expectedUpdatedRows);
//        Mockito.when(bankAccountRepository.updateCurrencyBy(toCurrency, userId)).thenReturn(expectedUpdatedRows);
//
//        int actualUpdatedRows = bankAccountService.updateBankAccount(bankAccountRequestCurrencyChange, userId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bankAccountService.updateBankAccount(bankAccountRequestCurrencyChange, userName));
        String expectedMessage = "Invalid input data for bankAccount currency change!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

//        assertEquals(expectedUpdatedRows * 2, actualUpdatedRows);
    }
}