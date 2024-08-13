package com.academy.cakeshop.controller;

import com.academy.cakeshop.mail.EmailService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/api/v1/emailService/contracts/rent")
    @PreAuthorize("hasRole('STORE')")
    public String sendRentContractMail(@RequestParam double rent) {
        emailService.sendRentEmail(rent);
        return "Rent contract sent!\nИзпратен договор за наем!";
    }

    @GetMapping("/api/v1/emailService/contracts/rent/approved")
    @PreAuthorize("hasRole('MALL')")
    public String sendApprovedRentContractMail(Principal principal, @RequestParam double rent) {
        emailService.sendRentApprovalEmail(rent, principal.getName());
        return "Approved rent contract sent!\nИзпратенo одобрение за договор за наем!";
    }

    @GetMapping("/api/v1/emailService/payments")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String sendMail(Principal principal,
                                      @RequestParam @Positive double amount,
                                      @RequestParam @Positive Long supplyOrderId,
                                      @RequestParam @Positive Long contractId) {
        emailService.sendEarlyPaymentNotice(amount, supplyOrderId, contractId, principal.getName());
        return "Early payment notice sent!\nИзпратена молба за предварително плащане!";
    }

}