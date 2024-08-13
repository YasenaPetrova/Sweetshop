//package com.academy.cakeshop.in;
//
//import com.academy.cakeshop.enumeration.Role;
//import com.academy.cakeshop.persistance.entity.User;
//import com.academy.cakeshop.persistance.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//@Component
//public class DBOperationRunner implements CommandLineRunner {
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//    @Autowired
//    UserRepository userRepository;
//
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        userRepository.saveAll(Arrays.asList(
//                new User(1001L, "Christina", "Ablanska", "chrisiAb", passwordEncoder.encode("1234"),
//                        "ch.ablanska@gmail.com", "0886 21 22 23", "St Petersberg Str, 23", Role.ADMIN),
//                new User(1002L, "Hristina", "Balkanska", "hrisiBa", passwordEncoder.encode("3254"),
//                        "hristina.bakanska@gmail.com", "0887 21 20 25", "Moscow Str, 2", Role.SUPPLIER),
//                new User(1003L, "Georgy", "Yanakiev", "geoYa", passwordEncoder.encode("1111"),
//                        "georgi.yanakiev@gmail.com", "0888 33 44 33", "Morska Str, 18", Role.CLIENT)
//        ));
//
//        userRepository.deleteById(153L);
//        System.out.println("----------All Data saved into Database----------------------");
//    }
//}