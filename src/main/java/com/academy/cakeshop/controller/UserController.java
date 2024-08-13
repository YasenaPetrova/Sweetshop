package com.academy.cakeshop.controller;

import com.academy.cakeshop.dto.*;
import com.academy.cakeshop.errorHandling.AccessDenied;
import com.academy.cakeshop.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final ContractService contractService;
    private final BankAccountService bankAccountService;
//    private final ReviewService reviewService;
    private final PaymentService paymentService;
    private final SaleService saleService;
    private final PurchaseOrderService purchaseOrderService;

    @GetMapping("/api/v1/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE')")
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(required = false) String lastName) {
        if (lastName == null) {
            List<UserResponse> users = userService.getAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            List<UserResponse> userResponseList = userService.getByLastName(lastName);
            return new ResponseEntity<>(userResponseList, HttpStatus.OK);
        }
    }

    @GetMapping("/api/v1/users/self")
    @PreAuthorize("hasAnyRole('STORE', 'MALL', 'SUPPLIER', 'MANAGER', 'EMPLOYEE', 'CLIENT', 'ADMIN')")
    public ResponseEntity<UserDetailsDTO> getById(Principal principal) {
        UserDetailsDTO userDetailsDTO = userService.getByUserName(principal.getName());
        return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
    }

    // Ststs
    @GetMapping("/api/v1/users/payments/stats")
    @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<EmployeePaymentDTO>> getEmployeePaymentStat(Principal principal) {
        List<EmployeePaymentDTO> employeePaymentDTOList = paymentService.employeeStatistics(principal.getName());
        return new ResponseEntity<>(employeePaymentDTOList, HttpStatus.OK);
    }

    @GetMapping("/api/v1/users/sales/stats")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<SaleStatDTO>> getStoreSaleStat() {
        List<SaleStatDTO> saleStatDTOList = saleService.getArticlesToSell();
        return new ResponseEntity<>(saleStatDTOList, HttpStatus.OK);
    }

    @GetMapping("/api/v1/users/sales/stats/days")
    @PreAuthorize("hasRole('MANAGER')")
    public String getStoreSaleStatDaysLeftToPayRent() {
        int daysLeftToPayRent = saleService.getHowManyDaysLeftToPayRent();
        return "Days left to pay rent: " + daysLeftToPayRent
                + "\nОставащи дни до събиране на сумата за наем: " + daysLeftToPayRent;
    }

    @GetMapping("/api/v1/users/purchases")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<PurchaseStatDTO>> getPurchaseOrderStats(Principal principal) {
        List<PurchaseStatDTO> purchaseStatDTOList = purchaseOrderService.getSupplierPurchaseStatistics(principal.getName());
        return new ResponseEntity<>(purchaseStatDTOList, HttpStatus.OK);
    }

    @PostMapping(value = "/public/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("permitAll()")
    public String createNewUser(@Valid @RequestBody NewUserAccountDTO newUserAccountDTO) {
        if (newUserAccountDTO.role().equalsIgnoreCase("CLIENT")) {
            UserRequest userRequest = userService.convertToUserRequest(newUserAccountDTO);
            userService.create(userRequest);
        } else {
            userService.createNewUser(newUserAccountDTO);
        }
        return "Account created!\nПотребителят е създаден!";
    }

//    @PostMapping(value = "/api/v1/users/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('CLIENT')")
//    public String addReview(Principal principal, @RequestParam @NotNull String comment) {
//        reviewService.create(comment, principal.getName());
//        return "Review sent!\nКоментарът е добавен!";
//    }

    @DeleteMapping(value = "/api/v1/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@PathVariable @NotNull(message = "Required field!")
                                        @Min(value = 1, message = "No negative values allowed!") Long id) {
        userService.deleteUserByID(id);
        return "Account deleted!\nПотребителят е изтрит!";
    }

    @GetMapping("/api/v1/users/contracts/{userId}")
    @PreAuthorize("hasAnyRole('STORE', 'MALL', 'SUPPLIER', 'MANAGER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<ContractResponse>> getContractByUserId(@NotNull(message = "Required field!")
                                                                      @Min(value = 1, message = "No negative values allowed")
                                                                      @PathVariable(name = "userId")
                                                                      Long id) {
        List<ContractResponse> contractResponseList = contractService.getByUserID(id);
        return new ResponseEntity<>(contractResponseList, HttpStatus.OK);
    }

    @GetMapping("/api/v1/users/bankAccounts/{userId}")
    @PreAuthorize("hasAnyRole('STORE', 'MALL', 'SUPPLIER', 'MANAGER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<BankAccountResponse> getByUserId(@NotNull(message = "Required field!")
                                                           @Min(value = 1, message = "No negative values allowed")
                                                           @RequestParam(name = "userId")
                                                           Long userId) {
        BankAccountResponse bankAccountResponse = bankAccountService.getByUserID(userId);
        return new ResponseEntity<>(bankAccountResponse, HttpStatus.OK);
    }

    @PatchMapping("/api/v1/users/bankAccounts/currency")
    @PreAuthorize("hasAnyRole('SUPPLIER', 'ADMIN')")
    public String updateAccount(Principal principal, @RequestBody BankAccountRequestCurrencyChange bankAccountRequestCurrencyChange) {
        try {
            int updatedRows = bankAccountService.updateBankAccount(bankAccountRequestCurrencyChange, principal.getName());
            if (updatedRows > 0) {return "Currency successfully changed!\nВалутата бе успешно променена!";}
            else return "Something went wrong!\nВъзникна проблем!";
        } catch (AccessDenied exception) {
            return exception.getMessage();
        }
    }

    @PatchMapping("/api/v1/users/bankAccounts/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER')")
    public String closeAccount(Principal principal, @RequestParam(name = "iban")
                                            @NotNull(message = "Required field!")
                                           String iban) {
        try {
            bankAccountService.closeAccount(iban, principal.getName());
            return "Account successfully closed!\nСметката беше успешно закрита!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}