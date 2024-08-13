package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.PaymentRequest;
import com.academy.cakeshop.dto.PaymentResponse;
import com.academy.cakeshop.enumeration.*;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.mail.EmailService;
import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.persistance.entity.Contract;
import com.academy.cakeshop.persistance.entity.Payment;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class PaymentServiceTest {
    PaymentService paymentService;
    PaymentRepository paymentRepository;
    BankAccountRepository bankAccountRepository;
    ContractRepository contractRepository;
    AccountHistoryRepository accountHistoryRepository;
    UserRepository userRepository;
    EmailService emailService;
    BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        paymentRepository = Mockito.mock(PaymentRepository.class);
        bankAccountRepository = Mockito.mock(BankAccountRepository.class);
        contractRepository = Mockito.mock(ContractRepository.class);
        accountHistoryRepository = Mockito.mock(AccountHistoryRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        emailService = Mockito.mock(EmailService.class);
        bankAccountService = Mockito.mock(BankAccountService.class);
        paymentService = new PaymentService(paymentRepository, bankAccountRepository, contractRepository,
                accountHistoryRepository, userRepository, emailService, bankAccountService);
    }

    @Test
    void givenValidId_whenGettingPaymentById_thenReturnCorrectPayment() {
        Long id = 1L;
        Mockito.when(paymentRepository.existsById(id)).thenReturn(true);

        User user = new User(id, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Contract contract = new Contract(id, 200.0, Currency.BGN, ContractPeriod.MONTHLY,
                ContractStatus.APPROVED, user);
        BankAccount bankAccount1 = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        BankAccount bankAccount2 = new BankAccount(2L, "GB33BUKB20201555666664",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        Payment payment = new Payment(id, LocalDate.now(), contract, bankAccount1, bankAccount2, 200.0, Currency.BGN);
        Mockito.when(paymentRepository.getReferenceById(id)).thenReturn(payment);

        PaymentResponse expectedPaymentResponse = new PaymentResponse(payment.getFromBankAccount().getIban(),
                payment.getToBankAccount().getIban(),
                payment.getAmount(), payment.getCurrency().toString());
        PaymentResponse actualPaymentResponse = paymentService.getByID(id);
        assertEquals(expectedPaymentResponse, actualPaymentResponse);
    }

    @Test
    void givenInvalidId_whenGettingPaymentById_thenExceptionThrown() {
        Long id = 1L;
        Mockito.when(paymentRepository.existsById(id)).thenReturn(false);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> paymentService.getByID(id));
        String expectedMessage = "No payment with id: " + id + " found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenGettingAllPaymentsByContractID_thenReturnAListOfThem() {
        Long id = 1L;
        Mockito.when(paymentRepository.existsById(id)).thenReturn(true);

        User user = new User(id, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Contract contract = new Contract(id, 200.0, Currency.BGN, ContractPeriod.MONTHLY,
                ContractStatus.APPROVED, user);
        BankAccount bankAccount1 = new BankAccount(1L, "GB33BUKB20201555555555",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        BankAccount bankAccount2 = new BankAccount(2L, "GB33BUKB20201555666664",
                100.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        Payment payment1 = new Payment(1L, LocalDate.now(), contract, bankAccount1, bankAccount2, 200.0, Currency.BGN);
        Payment payment2 = new Payment(2L, LocalDate.now(), contract, bankAccount1, bankAccount2, 240.0, Currency.EUR);
        List<Payment> payments = new ArrayList<>();
        payments.add(payment1);
        payments.add(payment2);

        Mockito.when(paymentRepository.findByContractID(id)).thenReturn(payments);
        List<PaymentResponse> expectedPaymentResponseList = new ArrayList<>();
        PaymentResponse expectedPaymentResponse1 = new PaymentResponse(payment1.getFromBankAccount().getIban(),
                payment1.getToBankAccount().getIban(),
                payment1.getAmount(), payment1.getCurrency().toString());
        PaymentResponse expectedPaymentResponse2 = new PaymentResponse(payment2.getFromBankAccount().getIban(),
                payment2.getToBankAccount().getIban(),
                payment2.getAmount(), payment2.getCurrency().toString());
        expectedPaymentResponseList.add(expectedPaymentResponse1);
        expectedPaymentResponseList.add(expectedPaymentResponse2);

        List<PaymentResponse> actualPaymentResponseList = paymentService.getAllByContractID(id);
        assertEquals(expectedPaymentResponseList, actualPaymentResponseList);
    }

    @Test
    void givenInvalidId_whenGettingAllPaymentsByContractID_thenExceptionThrown() {
        Long id = 1L;
        Mockito.when(paymentRepository.findByContractID(id)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> paymentService.getAllByContractID(id));
        String expectedMessage = "No payment with contractId: " + id + " found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenCheckingIfPaymentExistsById_thenReturnTrue() {
        Long id = 5L;
        Mockito.when(paymentRepository.existsById(id)).thenReturn(true);
        assertTrue(paymentService.existsByID(id));
    }

    @Test
    void givenValidId_whenCheckingIfPaymentExistsById_thenReturnFalse() {
        Long id = 5L;
        Mockito.when(paymentRepository.existsById(id)).thenReturn(false);
        assertFalse(paymentService.existsByID(id));
    }

    @Test
    void create() {
        Long contractID = 1L;
        String userName = "hrisiA";
        Contract contract = new Contract();
        contract.setId(contractID);
        Currency currency = Currency.BGN;
        PaymentRequest paymentRequest = new PaymentRequest(contractID, "DE75512108001245126199",
                "GB33BUKB20201555555555", 150.0, currency.toString());
        User user = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);

        BankAccount fromBankAccount = new BankAccount(1L, "DE75512108001245126199",
                2000.0, Currency.BGN, BankAccountStatus.ACTIVE, user);
        BankAccount toBankAccount = new BankAccount(5L, "GB33BUKB20201555555555",
                1000.0, Currency.BGN, BankAccountStatus.ACTIVE, user);

        Mockito.when(bankAccountRepository.findByIBAN(paymentRequest.fromIBAN())).thenReturn(fromBankAccount);
        Mockito.when(bankAccountRepository.findByIBAN(paymentRequest.toIBAN())).thenReturn(toBankAccount);

        Payment payment = new Payment();
        payment.setDate(LocalDate.now());
        payment.setFromBankAccount(bankAccountRepository.findByIBAN(paymentRequest.fromIBAN()));
        payment.setToBankAccount(bankAccountRepository.findByIBAN(paymentRequest.toIBAN()));
        Mockito.when(contractRepository.getReferenceById(paymentRequest.contractID())).thenReturn(contract);
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(user);
        payment.setContract(contract);
        payment.setAmount(paymentRequest.amount());
        payment.setCurrency(Currency.getCurrencyFromString(paymentRequest.currency()));

        paymentService.create(paymentRequest, userName);
        Mockito.verify(paymentRepository, Mockito.times(1)).saveAndFlush(any());
        Mockito.verify(accountHistoryRepository, Mockito.times(1)).saveAndFlush(any());
        Mockito.verify(bankAccountService, Mockito.times(1)).moneyTransfer(fromBankAccount,
                toBankAccount, paymentRequest.amount(), currency);
    }

    @Test
    void givenValidId_whenDeletingPaymentById_thenDeletePayment() {
        Long id = 1L;
        Mockito.when(paymentRepository.existsById(id)).thenReturn(true);
        paymentService.deleteByID(id);
        Mockito.verify(paymentRepository, Mockito.times(1)).deleteById(id);

    }

    @Test
    void givenValidId_whenDeletingPaymentById_thenExceptionThrown() {
        Long id = 1L;
        Mockito.when(paymentRepository.existsById(id)).thenReturn(false);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> paymentService.deleteByID(id));
        String expectedMessage = "No payment with id: " + id + " found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

    }
}