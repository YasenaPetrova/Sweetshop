package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.EmployeePaymentDTO;
import com.academy.cakeshop.dto.PaymentRequest;
import com.academy.cakeshop.dto.PaymentResponse;
import com.academy.cakeshop.enumeration.ContractStatus;
import com.academy.cakeshop.enumeration.Currency;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.errorHandling.FailedMoneyTransaction;
import com.academy.cakeshop.mail.EmailService;
import com.academy.cakeshop.mail.dto.EmailDto;
import com.academy.cakeshop.persistance.entity.*;
import com.academy.cakeshop.persistance.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ContractRepository contractRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BankAccountService bankAccountService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public PaymentResponse getByID(Long id) {
        if (existsByID(id)) {
            Payment payment = paymentRepository.getReferenceById(id);
            logger.info("Request to DB: getPayment by id: " + id);
            return new PaymentResponse(payment.getFromBankAccount().getIban(), payment.getToBankAccount().getIban(),
                    payment.getAmount(), payment.getCurrency().toString());
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No payment with id: " + id + " found!");
            logger.error("Error: No payment with id: {} found!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    public List<PaymentResponse> getAllByContractID(Long id) {
        List<Payment> payments = paymentRepository.findByContractID(id);
        if (payments != null) {
            List<PaymentResponse> paymentResponseList = new ArrayList<>();
            for (Payment payment : payments) {
                PaymentResponse paymentResponse = new PaymentResponse(payment.getFromBankAccount().getIban(), payment.getToBankAccount().getIban(),
                        payment.getAmount(), payment.getCurrency().toString());
                paymentResponseList.add(paymentResponse);
            }
            logger.info("Request to DB: getPayments by contractId: " + id);
            return paymentResponseList;
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No payments with contractId: " + id + " found!");
            logger.error("Error: No payments with contractId: {} found!", id, businessNotFound);
            throw businessNotFound;
        }
    }


    public boolean existsByID(Long id) {
        logger.info("Request to DB: check for existing contractId: " + id);
        return paymentRepository.existsById(id);
    }

    @Transactional
    public void create(PaymentRequest paymentRequest, String userName) {
        BankAccount fromBankAccount = bankAccountRepository.findByIBAN(paymentRequest.fromIBAN());
        BankAccount toBankAccount = bankAccountRepository.findByIBAN(paymentRequest.toIBAN());

        if (fromBankAccount == null || toBankAccount == null) {
            BusinessNotFound businessNotFound = new BusinessNotFound("Invalid IBAN or IBANs!");
            logger.error("Error: Invalid IBAN or IBANs!");
            throw businessNotFound;
        } else if (fromBankAccount.getBalance() < paymentRequest.amount()) {
            FailedMoneyTransaction failedMoneyTransaction = new FailedMoneyTransaction(
                    "Not sufficient balance to transfer the required sum!\n"
                    + "Sender account IBAN: " + fromBankAccount.getIban() + "\n"
                    + "Receiver account IBAN: " + toBankAccount.getIban() + "\n"
                    + "Amount of money transferred: " + paymentRequest.amount());
            logger.error("Not sufficient balance to transfer the required sum!");
            throw failedMoneyTransaction;
        } else {
            Currency currency = Currency.getCurrencyFromString(paymentRequest.currency());
            bankAccountService.moneyTransfer(fromBankAccount, toBankAccount, paymentRequest.amount(), currency);
            logger.info("Request to BankAccountService: transfer: amount " + paymentRequest.amount()
                    + " " + currency
                    + " from IBAN: " + fromBankAccount.getIban()
                    + " to IBAN: " + toBankAccount.getIban());

            Payment payment = new Payment();
            payment.setDate(LocalDate.now());
            payment.setFromBankAccount(fromBankAccount);
            payment.setToBankAccount(toBankAccount);

            Contract contract = contractRepository.getReferenceById(paymentRequest.contractID());
            payment.setContract(contract);
            payment.setAmount(paymentRequest.amount());
            payment.setCurrency(Currency.getCurrencyFromString(paymentRequest.currency()));
            paymentRepository.saveAndFlush(payment);
            logger.info("Request to DB: create new payment");

            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setAmount(paymentRequest.amount());
            accountHistory.setCurrency(paymentRequest.currency());
            accountHistory.setDate(LocalDate.now());
            accountHistory.setFromAccount(fromBankAccount);
            accountHistory.setToAccount(toBankAccount);
            accountHistoryRepository.saveAndFlush(accountHistory);
            logger.info("Request to DB: create new accountHistory");

            User user = userRepository.findByUserName(userName);
            String body = "A payment has been made on "
                    + LocalDate.now()
                    + " in regard to contract with id: "
                    + paymentRequest.contractID() + "\n"
                    + "Sender account IBAN: " + fromBankAccount.getIban() + "\n"
                    + "Receiver account IBAN: " + toBankAccount.getIban() + "\n"
                    + "Amount of money transferred: " + paymentRequest.amount()
                    + " "
                    + currency
                    + "\nThis is a system generated message. In case you have not authorized " +
                    "this payment, please contact out support team at cakeshop_support@cakes.com";
            EmailDto emailDto = new EmailDto("New payment created", user.getEmail(), body);
            emailService.sendSimpleEmail(emailDto);
            logger.info("Request to EmailService: send notification for the payment to: " + user.getEmail());
        }
    }

    public void deleteByID(Long id) {
        if (existsByID(id)) {
            paymentRepository.deleteById(id);
            logger.info("Request to DB: delete payment with paymentId: " + id);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No payment with id: " + id + " found!");
            logger.error("Error: No payments with id: {} found!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    // Работниците и мениджъра проверяват колко са заработили в акаунтите си.

    public List<EmployeePaymentDTO> employeeStatistics(String userName) {
        User user = userRepository.findByUserName(userName);
        Contract contract = contractRepository.findByUserIdActive(user.getId(), ContractStatus.APPROVED);
        if (contract != null) {
            List<Payment> payments = paymentRepository.findByContractID(contract.getId());
            List<EmployeePaymentDTO> employeePaymentDTOList = new ArrayList<>();

            for (Payment payment : payments) {
                String name = user.getFirstName() + " " + user.getLastName();
                EmployeePaymentDTO employeePaymentDTO = new EmployeePaymentDTO(name, user.getRole().toString(),
                        payment.getDate(), payment.getAmount());
                employeePaymentDTOList.add(employeePaymentDTO);
            }
            logger.info("Request to DB: employee payments statistic for userName: " + userName);
            return employeePaymentDTOList;
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No payments for userName: " + userName + " found!");
            logger.error("Error: No payments for userName: {} found!", userName, businessNotFound);
            throw businessNotFound;
        }
    }
}