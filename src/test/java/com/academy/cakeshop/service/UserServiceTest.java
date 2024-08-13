package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.UserResponse;
import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.UserRepository;
import com.academy.cakeshop.mail.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    BankAccountService bankAccountService;
    ContractService contractService;
    EmailService emailService;



    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        bankAccountService = Mockito.mock(BankAccountService.class);
        contractService = Mockito.mock(ContractService.class);
        emailService = Mockito.mock(EmailService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder, bankAccountService, contractService, emailService);
    }

    @Test
    void givenValidId_whenGettingUserById_thenReturnCorrectUser() {
        Long id = 1L;
        User user = new User(id, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        Mockito.when(userRepository.getReferenceById(id)).thenReturn(user);
        UserResponse expectedUser = new UserResponse("Hristina", "Ablanska",
                "hr.abl@gmail.com", "STORE");
        UserResponse actualUser = userService.getById(id);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void givenInvalidId_whenGettingUserById_thenThrowException() {
        Long id = 10L;
        Mockito.when(userRepository.getReferenceById(id)).thenReturn(null);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> userService.getById(id));
        String expectedMessage = "Userid: " + id + " not found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenAvailableUsers_whenGettingAllUsers_thenReturningThem() {
        User user1 = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        User user2 = new User(2L, "Hristina", "Ablanska", "hrisiA",
                "1234", "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        User user3 = new User(3L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        UserResponse expectedUser1 = new UserResponse("Hristina", "Ablanska",
                "hr.abl@gmail.com", "STORE");
        UserResponse expectedUser2 = new UserResponse("Hristina", "Ablanska",
                "hr.abl@gmail.com", "STORE");
        UserResponse expectedUser3 = new UserResponse("Hristina", "Ablanska",
                "hr.abl@gmail.com", "STORE");
        List<UserResponse> expectedUserResponseList = new ArrayList<>();
        expectedUserResponseList.add(expectedUser1);
        expectedUserResponseList.add(expectedUser2);
        expectedUserResponseList.add(expectedUser3);

        List<UserResponse> actualUserResponseList = userService.getAll();
        assertEquals(expectedUserResponseList, actualUserResponseList);
    }

    @Test
    void givenNoUsers_whenGettingAllUsers_thenExceptionThrown() {
        List<User> users = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn(users);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> userService.getAll());
        String expectedMessage = "No users found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidLastName_whenGettingUsersByLastName_thenAListReturned() {
        String lastName = "Ablanska";
        User user1 = new User(1L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        User user2 = new User(2L, "Hristina", "Ablanska", "hrisiA",
                "1234", "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        User user3 = new User(3L, "Hristina", "Ablanska", "hrisiA", "1234",
                "hr.abl@gmail.com", "0888524163", "test", Role.STORE);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        Mockito.when(userRepository.findLastNameByLastNameLikeIgnoreCaseOrderByLastNameAsc(lastName)).thenReturn(users);

        UserResponse expectedUser1 = new UserResponse("Hristina", "Ablanska",
                "hr.abl@gmail.com", "STORE");
        UserResponse expectedUser2 = new UserResponse("Hristina", "Ablanska",
                "hr.abl@gmail.com", "STORE");
        UserResponse expectedUser3 = new UserResponse("Hristina", "Ablanska",
                "hr.abl@gmail.com", "STORE");
        List<UserResponse> expectedUserResponseList = new ArrayList<>();
        expectedUserResponseList.add(expectedUser1);
        expectedUserResponseList.add(expectedUser2);
        expectedUserResponseList.add(expectedUser3);

        List<UserResponse> actualUserResponseList = userService.getByLastName(lastName);
        assertEquals(expectedUserResponseList, actualUserResponseList);
    }

    @Test
    void givenInvalidLastName_whenGettingUsersByLastName_thenAListReturned() {
        String lastName = "Ivanov";
        List<User> users = new ArrayList<>();
        Mockito.when(userRepository.findLastNameByLastNameLikeIgnoreCaseOrderByLastNameAsc(lastName)).thenReturn(users);

        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () -> userService.getByLastName(lastName));
        String expectedMessage = "No users with last name: " + lastName + " found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenCheckingIfUserExistsById_thenReturnTrue() {
        Long id = 5L;
        Mockito.when(userRepository.existsById(id)).thenReturn(true);
        assertTrue(userService.existsById(id));
    }

    @Test
    void givenInvalidId_whenCheckingIfUserExistsById_thenReturnFalse() {
        Long id = 5L;
        Mockito.when(userRepository.existsById(id)).thenReturn(false);
        assertFalse(userService.existsById(id));
    }

//    @Test
//    void create() {
//        UserRequest userRequest = new UserRequest("Hrisi", "Ablanska", "hrisA", "1234",
//                "hrisi@gmail.com", "0885232562", "St Nikolas St, 9", "STORE");
//        User user = new User();
//        user.setFirstName(userRequest.firstName());
//        user.setLastName(userRequest.lastName());
//        user.setUserName(userRequest.userName());
//        user.setPassword("1234");
//        user.setEmail(userRequest.email());
//        user.setPhoneNumber(userRequest.phoneNumber());
//        user.setAddress(userRequest.address());
//        user.setRole(Role.getRoleFromString(userRequest.role()));
//
//        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("testpass");
//        Mockito.when(userRepository.save(any()));
//
//        userService.create(userRequest);
//        Mockito.verify(userRepository.save(any()));
//    }

    @Test
    void givenValidId_whenUpdatingUserFirstName_thenReturnCorrectRowsUpdatedCount() {
        String firstName = "Christina";
        Long id = 1L;
        int expectedRowsUpdated = 1;
        Mockito.when(userRepository.updateUserFirstNameById(firstName, id)).thenReturn(expectedRowsUpdated);
        Mockito.when(userRepository.existsById(id)).thenReturn(true);
        int actualRowsUpdated = userService.updateUserFirstName(firstName, id);
        assertEquals(expectedRowsUpdated, actualRowsUpdated);
    }

    @Test
    void givenInvalidId_whenUpdatingUserFirstName_thenExceptionThrown() {
        String firstName = "Christina";
        Long id = 1L;
        Mockito.when(userRepository.existsById(id)).thenReturn(false);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                userService.updateUserFirstName(firstName, id));
        String expectedMessage = "Userid: " + id + " not found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenUpdatingUserLastName_thenReturnCorrectRowsUpdatedCount() {
        String lastName = "Petrova";
        Long id = 1L;
        int expectedRowsUpdated = 1;
        Mockito.when(userRepository.updateUserLastNameById(lastName, id)).thenReturn(expectedRowsUpdated);
        Mockito.when(userRepository.existsById(id)).thenReturn(true);
        int actualRowsUpdated = userService.updateUserLastName(lastName, id);
        assertEquals(expectedRowsUpdated, actualRowsUpdated);
    }

    @Test
    void givenInvalidId_whenUpdatingUserLastName_thenExceptionThrown() {
        String lastName = "Petrova";
        Long id = 1L;
        Mockito.when(userRepository.existsById(id)).thenReturn(false);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                userService.updateUserLastName(lastName, id));
        String expectedMessage = "Userid: " + id + " not found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void givenValidId_whenUpdatingUserEmail_thenReturnCorrectRowsUpdatedCount() {
        String email = "test@gmail.com";
        Long id = 1L;
        int expectedRowsUpdated = 1;
        Mockito.when(userRepository.updateUserEmailById(email, id)).thenReturn(expectedRowsUpdated);
        Mockito.when(userRepository.existsById(id)).thenReturn(true);
        int actualRowsUpdated = userService.updateUserEmailById(email, id);
        assertEquals(expectedRowsUpdated, actualRowsUpdated);
    }

    @Test
    void givenInvalidId_whenUpdatingUserEmail_thenExceptionThrown() {
        String email = "test@gmail.com";
        Long id = 1L;
        Mockito.when(userRepository.existsById(id)).thenReturn(false);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                userService.updateUserEmailById(email, id));
        String expectedMessage = "Userid: " + id + " not found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    //    TODO How to test delete, ask Nadeto
    @Test
    void givenValidId_whenDeletingUserById_thenDeleteUser() {
        Long id = 1L;
        Mockito.when(userRepository.existsById(id)).thenReturn(true);
        userService.deleteUserByID(id);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(id);

    }

    @Test
    void givenInvalidId_whenDeletingUserById_thenExceptionThrown() {
        Long id = 1L;
        Mockito.when(userRepository.existsById(id)).thenReturn(false);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class, () ->
                userService.deleteUserByID(id));
        String expectedMessage = "Userid: " + id + " not found!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}