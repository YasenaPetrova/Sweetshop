package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.*;
import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.mail.EmailService;
import com.academy.cakeshop.mail.dto.EmailDto;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankAccountService bankAccountService;
    private final ContractService contractService;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserResponse getById(Long id) {
        User user = userRepository.getReferenceById(id);
        if (user != null) {
            logger.info("Request to DB: getUser by id: " + id);
            return new UserResponse(user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getRole().toString());
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("Userid: " + id + " not found!");
            logger.error("Error: getById: Userid: {} not found!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    public UserDetailsDTO getByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        logger.info("Request to DB: get user with userName: {}", userName);
        if (user != null) {
            logger.info("Request to DB: get bankAccount by userId: {}", user.getId());
            BankAccountResponse bankAccountResponse = bankAccountService.getByUserID(user.getId());
            logger.info("Request to Service: return user account info: userName= {}", userName);
            String fullName = user.getFirstName() + " " + user.getLastName();
            return new UserDetailsDTO(fullName, userName, user.getEmail(), user.getRole().toString(),
                    bankAccountResponse.iban(), bankAccountResponse.currency(),
                    bankAccountResponse.balance(), bankAccountResponse.status());
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("UserName: " + userName + " not found!");
            logger.error("Error: getByUserName: userName: {} not found!", userName, businessNotFound);
            throw businessNotFound;
        }
    }

    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            List<UserResponse> userResponseList = new ArrayList<>();

            for (User user : users) {
                UserResponse userResponse = new UserResponse(
                        user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().toString());
                userResponseList.add(userResponse);
            }
            logger.info("Request to DB: getAllUsers");
            return userResponseList;
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No users found!" +
                    "\nНе са открити никакви потребители");
            logger.error("No users found!", businessNotFound);
            throw businessNotFound;
        }
    }

    public List<UserResponse> getByLastName(String lastName) {
        List<User> users = userRepository.findLastNameByLastNameLikeIgnoreCaseOrderByLastNameAsc(lastName);
        if (!users.isEmpty()) {
            List<UserResponse> userResponseList = new ArrayList<>();

            for (User user : users) {
                UserResponse userResponse = new UserResponse(
                        user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().toString());
                userResponseList.add(userResponse);
            }
            logger.info("Request to DB: getByLastName: " + lastName);
            return userResponseList;
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No users with last name: "
                    + lastName + " found!"
            + "\nне са открити потреители с фамилия " + lastName);
            logger.error("No users with last name: {} found!", lastName, businessNotFound);
            throw businessNotFound;
        }
    }

    public boolean existsById(Long id) {
        logger.info("Request to DB: user existsById: " + id);
        return userRepository.existsById(id);
    }

    public void create(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setUserName(userRequest.userName());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setEmail(userRequest.email());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setAddress(userRequest.address());
        user.setRole(Role.getRoleFromString(userRequest.role()));
        logger.info("Request to DB: create new user");
        userRepository.save(user);
    }

    public int updateUserFirstName(String firstName, Long id) {
        if (existsById(id)) {
            logger.info("Request to DB: updateUserFirstName: userId" + id + " with + " + firstName);
            return userRepository.updateUserFirstNameById(firstName, id);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("Userid: " + id + " not found!");
            logger.error("Userid: {} not found!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    public int updateUserLastName(String lastName, Long id) {
        if (existsById(id)) {
            logger.info("Request to DB: updateUserLastName: userId" + id + " with + " + lastName);
            return userRepository.updateUserLastNameById(lastName, id);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("Userid: " + id + " not found!");
            logger.error("Userid: {} not found!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    public int updateUserEmailById(String email, Long id) {
        if (existsById(id)) {
            logger.info("Request to DB: updateUserEmail: userId" + id + " with + " + email);
            return userRepository.updateUserEmailById(email, id);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("Userid: " + id + " not found!");
            logger.error("Userid: {} not found!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    public void deleteUserByID(Long id) {
        if (existsById(id)) {
            logger.info("Request to DB: delete user with id:" + id);
            userRepository.deleteById(id);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("Userid: " + id + " not found!");
            logger.error("Userid: {} not found!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    public int updateById(UserUpdateDTO userUpdateDTO) {
        int updatedRows = 0;
        Long userId = userUpdateDTO.id();
        updatedRows += updateUserFirstName(userUpdateDTO.firstName(), userId);
        updatedRows += updateUserLastName(userUpdateDTO.lastName(), userId);
        updatedRows += updateUserEmailById(userUpdateDTO.email(), userId);
        logger.info("Request to DB: updateById:" + userUpdateDTO.id());
        return updatedRows;
    }

    // User Creation


    public void createNewUser(NewUserAccountDTO newUserAccountDTO) {
        UserRequest userRequest = convertToUserRequest(newUserAccountDTO);
        BankAccountRequest bankAccountRequest = convertToBankAccountRequest(newUserAccountDTO);
        double contractSum = newUserAccountDTO.contractSum();
        logger.info("Request to DB: create new user with userName:" + newUserAccountDTO.userName());
        create(userRequest);
        logger.info("Request to DB: create new bankAccount with IBAN:" + newUserAccountDTO.iban());
        bankAccountService.create(bankAccountRequest, userRequest.userName());
        EmailDto emailDtoNewContract = new EmailDto();

        Role role = Role.getRoleFromString(userRequest.role());
        switch (role) {
            case MALL -> {
                ContractRequest contractRequest = new ContractRequest(contractSum, bankAccountRequest.currency(),
                        "MONTHLY", "NEW");
                logger.info("Request to DB: create new contract with amount:" + contractSum);
                contractService.create(contractRequest, userRequest.userName());
                emailDtoNewContract.setSubject("New lease contract created");
                emailDtoNewContract.setToList(userRequest.email());
                emailDtoNewContract.setBody("New contract created for monthly lease amount: " + contractSum + ".\n" +
                        "Waiting for approval!");
            }
            case SUPPLIER -> {
                ContractRequest contractRequest = new ContractRequest(contractSum, bankAccountRequest.currency(),
                        "DAILY", "APPROVED");
                logger.info("Request to DB: create new contract with amount:" + contractSum);
                contractService.create(contractRequest, userRequest.userName());
                emailDtoNewContract.setSubject("New purchase daily contract created");
                emailDtoNewContract.setToList(userRequest.email());
                emailDtoNewContract.setBody("New contract created for daily purchase limit - amount: " + contractSum);
            }
            case MANAGER, EMPLOYEE, ADMIN -> {
                ContractRequest contractRequest = new ContractRequest(contractSum, bankAccountRequest.currency(),
                        "MONTHLY", "APPROVED");
                logger.info("Request to DB: create new contract with amount:" + contractSum);
                contractService.create(contractRequest, userRequest.userName());
                emailDtoNewContract.setSubject("New purchase employee contract created");
                emailDtoNewContract.setToList(userRequest.email());
                emailDtoNewContract.setBody("New contract created for monthly salary - amount: " + contractSum);
            }
            default -> {
                break;
            }
        }

//        String emailBody = "New user account has been created\n" +
//                "username: " + userRequest.userName() +"\n";
//        EmailDto emailDtoNewUser = new EmailDto("New User Created", userRequest.email(), emailBody);
//        emailService.sendMail(emailDtoNewUser);
//        if (!emailDtoNewContract.isEmpty()) {
//        logger.info("Email with contract details sent to:" + newUserAccountDTO.email());
//            emailService.sendMail(emailDtoNewContract);
//        }
    }

    public UserRequest convertToUserRequest(NewUserAccountDTO newUserAccountDTO) {
        logger.info("Request to UserService: convertToUserRequest");
        return new UserRequest(newUserAccountDTO.firstName(), newUserAccountDTO.lastName(),
                newUserAccountDTO.userName(), newUserAccountDTO.password(),
                newUserAccountDTO.email(), newUserAccountDTO.phoneNumber(),
                newUserAccountDTO.address(), newUserAccountDTO.role());
    }

    public BankAccountRequest convertToBankAccountRequest(NewUserAccountDTO newUserAccountDTO) {
        logger.info("Request to UserService: convertToBankAccountRequest");
        return new BankAccountRequest(newUserAccountDTO.iban(), newUserAccountDTO.balance(),
                newUserAccountDTO.currency());
    }

}