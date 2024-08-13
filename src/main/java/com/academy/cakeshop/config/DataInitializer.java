//package com.academy.cakeshop.config;
//
//import com.academy.cakeshop.enumeration.BankAccountStatus;
//import com.academy.cakeshop.enumeration.Currency;
//import com.academy.cakeshop.enumeration.Role;
//import com.academy.cakeshop.persistance.entity.BankAccount;
//import com.academy.cakeshop.persistance.entity.User;
//import com.academy.cakeshop.persistance.repository.BankAccountRepository;
//import com.academy.cakeshop.persistance.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@Configuration
//public class DataInitializer {
//    @Bean
//    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder, BankAccountRepository bankAccountRepository) {
//        return args -> {
//            User admin = new User();
//            admin.setUserName("admin");
//            admin.setPassword(passwordEncoder.encode("adminpass"));
//            admin.setRole(Role.ADMIN);
//            admin.setInitialRole();
//            admin.setFirstName("Jane");
//            admin.setLastName("Doe");
//            userRepository.save(admin);
//
//            User user = new User();
//            user.setUserName("user");
//            user.setPassword(passwordEncoder.encode("userpass"));
//            user.setRole(Role.STORE);
//            user.setInitialRole();
//            user.setFirstName("Cossete");
//            user.setLastName("Sweeters");
//            userRepository.save(user);
//
//            User mall = new User();
//            mall.setUserName("mall");
//            mall.setPassword(passwordEncoder.encode("mallpass"));
//            mall.setRole(Role.MALL);
//            mall.setInitialRole();
//            mall.setFirstName("Linda");
//            mall.setLastName("Cane");
//            userRepository.save(mall);
//
//            User supplier = new User();
//            supplier.setUserName("supplier");
//            supplier.setPassword(passwordEncoder.encode("supplierpass"));
//            supplier.setRole(Role.SUPPLIER);
//            supplier.setInitialRole();
//            supplier.setFirstName("Justin");
//            supplier.setLastName("Loris");
//            userRepository.save(supplier);
//
//            User manager = new User();
//            manager.setUserName("manager");
//            manager.setPassword(passwordEncoder.encode("managerpass"));
//            manager.setRole(Role.MANAGER);
//            manager.setInitialRole();
//            manager.setFirstName("Matilda");
//            manager.setLastName("Davis");
//            userRepository.save(manager);
//
//            User employee = new User();
//            employee.setUserName("employee");
//            employee.setPassword(passwordEncoder.encode("managerpass"));
//            employee.setRole(Role.EMPLOYEE);
//            employee.setInitialRole();
//            employee.setFirstName("Samantha");
//            employee.setLastName("Robberts");
//            userRepository.save(employee);
//
//            User client = new User();
//            client.setUserName("client");
//            client.setPassword(passwordEncoder.encode("clientpass"));
//            client.setRole(Role.CLIENT);
//            client.setInitialRole();
//            client.setFirstName("Isaac");
//            client.setLastName("Ferguson");
//            userRepository.save(client);
//
//            BankAccount bankAccountEUR = new BankAccount();
//            bankAccountEUR.setIban("DE75512108001245126199");
//            bankAccountEUR.setBankAccountStatus(BankAccountStatus.ACTIVE);
//            bankAccountEUR.setCurrency(Currency.EUR);
//            bankAccountEUR.setBalance(20000.0);
//            bankAccountEUR.setUser(user);
//            bankAccountRepository.save(bankAccountEUR);
//
//            BankAccount bankAccountBGN = new BankAccount();
//            bankAccountBGN.setIban("GB33BUKB20201555555555");
//            bankAccountBGN.setBankAccountStatus(BankAccountStatus.ACTIVE);
//            bankAccountBGN.setCurrency(Currency.BGN);
//            bankAccountBGN.setBalance(150000.0);
//            bankAccountBGN.setUser(user);
//            bankAccountRepository.save(bankAccountBGN);
//        };
//    }
//}