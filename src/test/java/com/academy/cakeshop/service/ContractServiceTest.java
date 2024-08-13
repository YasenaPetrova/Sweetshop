package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.ContractRequest;
import com.academy.cakeshop.dto.ContractResponse;
import com.academy.cakeshop.enumeration.ContractPeriod;
import com.academy.cakeshop.enumeration.ContractStatus;
import com.academy.cakeshop.enumeration.Currency;
import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.Contract;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.ContractRepository;
import com.academy.cakeshop.persistance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ContractServiceTest {
    @InjectMocks
    ContractService contractService;
    @Mock
    ContractRepository contractRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        contractRepository = Mockito.mock(ContractRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        contractService = new ContractService(contractRepository, userRepository);
    }

    @Test
    void givenValidId_whenGettingContractById_thenReturnCorrectContract() {
        Long id = 1L;
        User user = new User(id, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Contract contract = new Contract(id, 200.0, Currency.BGN, ContractPeriod.MONTHLY,
                ContractStatus.APPROVED, user);
        Mockito.when(contractRepository.getReferenceById(id)).thenReturn(contract);

        String contractorName = user.getFirstName() + " " + user.getLastName();
        ContractResponse expectedContractResponse = new ContractResponse(contract.getContractSum(), contract.getCurrency().toString(),
                contract.getContractPeriod().toString(), contractorName);
        ContractResponse actualContractResponse = contractService.getByID(id);
        assertEquals(expectedContractResponse, actualContractResponse);
    }


    @Test
    void givenInvalidId_whenGettingContractById_thenExceptionThrown() {
        Long id = 1L;
        User user = new User(id, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Contract contract = new Contract(id, 200.0, Currency.BGN, ContractPeriod.MONTHLY,
                ContractStatus.APPROVED, user);
        Mockito.when(contractRepository.getReferenceById(id)).thenReturn(null);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> contractService.getByID(id));
        String expectedMessage = "No contract with id: " + id + "found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenGettingContractByUserId_thenReturnCorrectContract() {
        Long userId = 1L;
        User user = new User(userId, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Contract contract1 = new Contract(1L, 200.0, Currency.BGN, ContractPeriod.MONTHLY,
                ContractStatus.APPROVED, user);
        Contract contract2 = new Contract(2L, 250.0, Currency.BGN, ContractPeriod.MONTHLY,
                ContractStatus.APPROVED, user);
        Contract contract3 = new Contract(3L, 300.0, Currency.BGN, ContractPeriod.MONTHLY,
                ContractStatus.APPROVED, user);
        List<Contract> contracts = new ArrayList<>();
        contracts.add(contract1);
        contracts.add(contract2);
        contracts.add(contract3);
        String contractorName = user.getFirstName() + " " + user.getLastName();

        Mockito.when(contractRepository.findAllByUserId(userId)).thenReturn(contracts);

        ContractResponse expectedContractResponse1 = new ContractResponse(contract1.getContractSum(), contract1.getCurrency().toString(),
                contract1.getContractPeriod().toString(), contractorName);
        ContractResponse expectedContractResponse2 = new ContractResponse(contract2.getContractSum(), contract2.getCurrency().toString(),
                contract2.getContractPeriod().toString(), contractorName);
        ContractResponse expectedContractResponse3 = new ContractResponse(contract3.getContractSum(), contract3.getCurrency().toString(),
                contract3.getContractPeriod().toString(), contractorName);

        List<ContractResponse> expectedContractResponseList = new ArrayList<>();
        expectedContractResponseList.add(expectedContractResponse1);
        expectedContractResponseList.add(expectedContractResponse2);
        expectedContractResponseList.add(expectedContractResponse3);

        List<ContractResponse> actualContractResponseList = contractService.getByUserID(userId);

        assertEquals(expectedContractResponseList, actualContractResponseList);
    }

    @Test
    void givenInvalidId_whenGettingContractByUserId_thenReturnCorrectContract() {
        Long userId = 1L;

        Mockito.when(contractRepository.findAllByUserId(userId)).thenReturn(null);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> contractService.getByUserID(userId));
        String expectedMessage = "No contracts with userId: " + userId + "found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenCheckingIfContractExistsById_thenReturnTrue() {
        Long id = 5L;
        Mockito.when(contractRepository.existsById(id)).thenReturn(true);
        assertTrue(contractService.existsByID(id));
    }

    @Test
    void givenInvalidId_whenCheckingIfContractExistsById_thenReturnFalse() {
        Long id = 5L;
        Mockito.when(contractRepository.existsById(id)).thenReturn(false);
        assertFalse(contractService.existsByID(id));
    }

    @Test
    void create() {
        ContractRequest contractRequest = new ContractRequest(250.0, "BGN", "MONTHLY", "APPROVED");
        String userName = "hrisiA";
        Contract expectedContract = new Contract();
        expectedContract.setContractSum(contractRequest.contractSum());
        expectedContract.setCurrency(Currency.getCurrencyFromString(contractRequest.currency()));
        expectedContract.setContractPeriod(ContractPeriod.getContractPeriodFromString(contractRequest.contractPeriod()));
        expectedContract.setContractStatus(ContractStatus.NEW);

        User user = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(user);
        expectedContract.setUser(user);
        contractService.create(contractRequest, userName);
        Mockito.verify(contractRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    void updateContractStatusTest() {
        String status = "APPROVED";
        Long contractId = 1L;
        ContractStatus contractStatus = ContractStatus.getContractStatusFromString(status);
        int expectedRowsUpdated = 1;
        Mockito.when(contractRepository.updateContractStatusById(contractStatus, contractId)).thenReturn(expectedRowsUpdated);
        int actualRowsUpdated = contractService.updateContractStatus(status, contractId);
        assertEquals(expectedRowsUpdated, actualRowsUpdated);
    }

    @Test
    void givenValidId_whenDeletingContractById_thenDeleteContract() {
        Long id = 1L;
        Mockito.when(contractRepository.existsById(id)).thenReturn(true);
        contractService.deleteById(id);
        Mockito.verify(contractRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void givenInvalidId_whenDeletingContractById_thenExceptionThrown() {
        Long id = 1L;
        Mockito.when(contractRepository.existsById(id)).thenReturn(false);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> contractService.deleteById(id));
        String expectedMessage = "No contract with id: " + id + "found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}