package com.academy.cakeshop.mail;

import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.errorHandling.EmailNotSent;
import com.academy.cakeshop.mail.dto.EmailDto;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.UserRepository;
import com.academy.cakeshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void sendSimpleEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getToList());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getBody());

        logger.info("Request to EmailService: send email with subject" + emailDto.getSubject()
        + " to email address: " + emailDto.getToList());
        mailSender.send(message);
    }

    public void sendRentEmail(double rent) {
        User user = userRepository.findByRole(Role.MALL);
        String subject = "New rent contract request";
        String toList = user.getEmail();
        String body = "Dear Sir/Madame " + user.getLastName() + ", \n"
                + "We would like to propose a deal for new monthly rent in the amount of "
                + rent
                + ".\nWe are looking forward for your approval.\n"
                + "Best regards,\nCakeShop team";
        try {
            EmailDto emailDto = new EmailDto(subject, toList, body);
            logger.info("Request to EmailService: send email with subject" + emailDto.getSubject()
                    + " to email address: " + emailDto.getToList());
            sendSimpleEmail(emailDto);
        } catch (EmailNotSent e) {
            System.out.println("Email not sent! Please try again!");
            logger.error("Error: Email for new rent request not sent! Proposed rent amount {}", rent, e);
        }
    }

    public void sendRentApprovalEmail(double rent, String userName) {
        User mall = userRepository.findByUserName(userName);
        User store = userRepository.findByRole(Role.STORE);
        String subject = "Rent Approval contract request";
        String toList = store.getEmail();
        String body = "Dear Sir/Madame " + store.getLastName() + ", \n"
                + "We would like to approve your proposal for new monthly rent in the amount of "
                + rent
                + "\nBest regards,\nMall team";
        try {
            EmailDto emailDto = new EmailDto(subject, toList, body);
            logger.info("Request to EmailService: send email with subject" + emailDto.getSubject()
                    + " to email address: " + emailDto.getToList());
            sendSimpleEmail(emailDto);

            EmailDto emailDtoCC = new EmailDto("Copy of rent approval", mall.getEmail(), body);
            logger.info("Request to EmailService: send email with subject" + emailDto.getSubject()
                    + " to email address: " + emailDto.getToList());
            sendSimpleEmail(emailDtoCC);
        } catch (EmailNotSent e) {
            System.out.println("Email not sent! Please try again!");
            logger.error("Error: Email for approve rent request not sent! Approved rent amount {}", rent, e);
        }
    }

    public void sendEarlyPaymentNotice(double amount, Long supplyOrderId, Long contractId, String userName) {
        User user = userRepository.findByUserName(userName);

        String subject = "Early Payment Notice - contractId: " + contractId;
        String toList = user.getEmail();
        String body = "Dear Mrs. Store,\n"
                + "Please note that I would like to receive early payment for supply order#: "
                + supplyOrderId
                + " in regard to contract with id: "
                + contractId
                + ".\nWe are looking forward for your approval.\n"
                + "Best regards,\n"
                + user.getFirstName() + " " + user.getLastName();
        try {
            EmailDto emailDto = new EmailDto(subject, toList, body);
            logger.info("Request to EmailService: send email with subject" + emailDto.getSubject()
                    + " to email address: " + emailDto.getToList());
            sendSimpleEmail(emailDto);
        } catch (EmailNotSent e) {
            System.out.println("Email not sent! Please try again!");
            logger.error("Error: Email for early payment not sent! Payment amount {}", amount, e);
        }
    }

}