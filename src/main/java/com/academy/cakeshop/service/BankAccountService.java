package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.BankAccountRequest;
import com.academy.cakeshop.dto.BankAccountRequestCurrencyChange;
import com.academy.cakeshop.dto.BankAccountResponse;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.errorHandling.AccessDenied;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.errorHandling.FailedMoneyTransaction;
import com.academy.cakeshop.mail.EmailService;
import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.BankAccountRepository;
import com.academy.cakeshop.enumeration.Currency;
import com.academy.cakeshop.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
//    @Value("${eur.to.bgn}")
//    private double conversionERUtoBGN;
//
//    @Value("${bgn.to.eur}")
//    private double conversionBGNtoEUR;

    public BankAccountResponse getByID(Long id) {
        BankAccount bankAccount = bankAccountRepository.getReferenceById(id);
        if (bankAccount != null) {
            String owner = bankAccount.getUser().getFirstName() + " " + bankAccount.getUser().getLastName();
            logger.info("Request to DB: getBankAccountByID by id: " + id);
            return new BankAccountResponse(bankAccount.getIban(),
                    bankAccount.getCurrency().toString(),
                    bankAccount.getBalance(), owner, bankAccount.getBankAccountStatus().toString());
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with id: " + id + "does not exist!");
            logger.error("BankAccount with id: {} does not exist!", id, businessNotFound);
            throw businessNotFound;
        }
    }

    public BankAccountResponse getByUserID(Long userID) {
        BankAccount bankAccount = bankAccountRepository.findByUserID(userID);
        if (bankAccount != null) {
            String owner = bankAccount.getUser().getFirstName() + " " + bankAccount.getUser().getLastName();
            logger.info("Request to DB: getBankAccountByUserID by userId: " + userID);
            return new BankAccountResponse(bankAccount.getIban(),
                    bankAccount.getCurrency().toString(),
                    bankAccount.getBalance(), owner, bankAccount.getBankAccountStatus().toString());
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with userID: " + userID + " does not exist!");
            logger.error("BankAccount with userID: {} does not exist!", userID, businessNotFound);
            throw businessNotFound;
        }

    }

    public List<BankAccountResponse> getAll() {
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        if (bankAccountList != null) {
            List<BankAccountResponse> bankAccountResponseList = new ArrayList<>();
            for (BankAccount bankAccount : bankAccountList) {
                String owner = bankAccount.getUser().getFirstName() + " " + bankAccount.getUser().getLastName();
                BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccount.getIban(),
                        bankAccount.getCurrency().toString(),
                        bankAccount.getBalance(), owner, bankAccount.getBankAccountStatus().toString());
                bankAccountResponseList.add(bankAccountResponse);
            }
            logger.info("Request to DB: getAllBankAccounts");
            return bankAccountResponseList;
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No bank accounts found!");
            logger.error("No bank accounts found!");
            throw businessNotFound;
        }
    }

    public boolean existsByIBAN(String IBAN) {
        BankAccount bankAccount = bankAccountRepository.findByIbanEquals(IBAN);
        logger.info("Request to Service: check for existing IBAN");
        return bankAccount != null;
    }

    public void create(BankAccountRequest bankAccountRequest, String userName) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban(bankAccountRequest.iban());
        bankAccount.setBalance(bankAccountRequest.balance());
        bankAccount.setCurrency(Currency.getCurrencyFromString(bankAccountRequest.currency()));
        bankAccount.setBankAccountStatus(BankAccountStatus.ACTIVE);
        User user = userRepository.findByUserName(userName);
        bankAccount.setUser(user);
        logger.info("Request to DB: create new bankAccount with IBAN: " + bankAccountRequest.iban()
        + " and user with userName: " + user.getUserName());
        bankAccountRepository.saveAndFlush(bankAccount);
    }

    public int updateBankAccountCurrency(Currency currency, Long userId) {
        BankAccount bankAccount = bankAccountRepository.findByUserID(userId);
        if (bankAccount != null) {
            logger.info("Request to DB: update currency of IBAN: " + bankAccount.getIban()
                    + " to " + currency.toString());
            return bankAccountRepository.updateCurrencyBy(currency, userId);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with userID: " + userId + "does not exist!");
            logger.error("BankAccount with userID: {} does not exist!", userId, businessNotFound);
            throw businessNotFound;
        }
    }

    public int updateBankAccountBalance(Double balance, Long userId) {
        BankAccount bankAccount = bankAccountRepository.findByUserID(userId);
        if (bankAccount != null) {
            logger.info("Request to DB: update balance of IBAN: " + bankAccount.getIban()
                    + " to " + balance);
            return bankAccountRepository.updateBalanceBy(balance, userId);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with userID: " + userId + "does not exist!");
            logger.error("BankAccount with userID: {} does not exist!", userId, businessNotFound);
            throw businessNotFound;
        }
    }

    public void deleteByID(String iban) {
        if (existsByIBAN(iban)) {
            BankAccount bankAccount = bankAccountRepository.findByIbanEquals(iban);
            logger.info("Request to DB: delete IBAN: " + iban);
            bankAccountRepository.deleteById(bankAccount.getId());
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with iban: " + iban + " does not exist!");
            logger.error("BankAccount with iban: {} does not exist!", iban, businessNotFound);
            throw businessNotFound;
        }
    }

    @Transactional
    public void closeAccount(String iban, String userName) {
        if (existsByIBAN(iban)) {
            User user = userRepository.findByUserName(userName);
            BankAccount bankAccount = bankAccountRepository.findByIbanEquals(iban);
            if (user.equals(bankAccount.getUser()) || user.getRole().equals(Role.ADMIN)) {
                if (bankAccount.getBankAccountStatus() == BankAccountStatus.ACTIVE) {
                    bankAccount.setBankAccountStatus(BankAccountStatus.INACTIVE);
                    bankAccountRepository.updateBankAccountStatus(BankAccountStatus.INACTIVE, iban);
                    logger.info("Request to DB: update bankAccount status to INACTIVE for IBAN: " + iban);
                } else {
                    IllegalArgumentException illegalArgumentException = new IllegalArgumentException("iban " + iban
                            + " is already CLOSED");
                    logger.error("iban {} is already CLOSED", iban, illegalArgumentException);
                    throw illegalArgumentException;
                }
            } else {
                AccessDenied accessDenied = new AccessDenied("Access denied to iban: " + iban);
                logger.error("Access denied to iban: {}", iban, accessDenied);
                throw accessDenied;
            }
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with iban: " + iban + " does not exist!");
            logger.error("BankAccount with iban: {} does not exist!", iban, businessNotFound);
            throw businessNotFound;
        }
    }

    public double getBalanceByIBAN(String iban) {
        if (existsByIBAN(iban)) {
            BankAccount bankAccount = bankAccountRepository.findByIbanEquals(iban);
            logger.info("Request to DB: getBalanceByIBAN for IBAN: " + iban);
            return bankAccount.getBalance();
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with iban: " + iban + " does not exist!");
            logger.error("BankAccount with iban: {} does not exist!", iban, businessNotFound);
            throw businessNotFound;
        }
    }

    @Transactional
    public BankAccount deposit(double sum, String iban) {
        if (existsByIBAN(iban)) {
            BankAccount bankAccount = bankAccountRepository.findByIbanEquals(iban);
            double balance = bankAccount.getBalance();
            sum = Math.abs(sum);
            balance = balance + sum;
            /*int rowsUpdated = */
            updateBankAccountBalance(balance, bankAccount.getId());
            logger.info("Request to BankAccountService: deposit " + sum + " to IBAN: " + iban);
//            if (rowsUpdated > 0) {
            return bankAccount;
//            } else throw new FailedMoneyTransaction("Unable to complete the deposit!");
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with iban: " + iban + " does not exist!");
            logger.error("BankAccount with iban: {} does not exist!", iban, businessNotFound);
            throw businessNotFound;
        }
    }

    @Transactional
    public BankAccount withdraw(double sum, String iban) {
        if (existsByIBAN(iban)) {
            BankAccount bankAccount = bankAccountRepository.findByIbanEquals(iban);
            double balance = bankAccount.getBalance();
            sum = Math.abs(sum);
            if (sum <= balance) {
                balance = balance - sum;
                updateBankAccountBalance(balance, bankAccount.getId());
                logger.info("Request to BankAccountService: withdraw " + sum + " from IBAN: " + iban);
                return bankAccount;
            } else {
                FailedMoneyTransaction failedMoneyTransaction = new FailedMoneyTransaction(
                        "Unable to complete transaction due to low account balance!");
                logger.error("Unable to complete transaction due to low account balance!");
                throw failedMoneyTransaction;
            }
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("BankAccount with iban: " + iban + " does not exist!");
            logger.error("BankAccount with iban: {} does not exist!", iban, businessNotFound);
            throw businessNotFound;
        }
    }

    @Transactional
    public void moneyTransfer(BankAccount fromAccount, BankAccount toAccount, double amount, Currency currency) {
        if (fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            if (fromAccount.getCurrency() == currency) {
                transfer(fromAccount, toAccount, amount);
                logger.info("Request to BankAccountService: transfer " + amount + " " + currency.toString()
                        + " from IBAN: " + fromAccount.getIban()
                        + " to IBAN: " + toAccount.getIban());
            } else {
                amount = getConvertedAmount(amount, currency);
                transfer(fromAccount, toAccount, amount);
                logger.info("Request to BankAccountService: transfer " + amount + " " + currency.toString()
                        + " from IBAN: " + fromAccount.getIban()
                        + " to IBAN: " + toAccount.getIban());
            }
        } else {
            if (fromAccount.getCurrency().equals(currency)) {
                withdraw(amount, fromAccount.getIban());
                amount = getConvertedAmount(amount, toAccount.getCurrency());
                deposit(amount, toAccount.getIban());
                logger.info("Request to BankAccountService: transfer " + amount + " " + currency.toString()
                        + " from IBAN: " + fromAccount.getIban()
                        + " to IBAN: " + toAccount.getIban());
            } else {
                deposit(amount, toAccount.getIban());
                amount = getConvertedAmount(amount, fromAccount.getCurrency());
                withdraw(amount, fromAccount.getIban());
                logger.info("Request to BankAccountService: transfer " + amount + " " + currency.toString()
                        + " from IBAN: " + fromAccount.getIban()
                        + " to IBAN: " + toAccount.getIban());
            }
        }
    }

    public static double getConvertedAmount(double amount, Currency currency) {
        return switch (currency) {
            case BGN -> amount * 1.96;
            case EUR -> amount * 0.51;
        };
    }

    @Transactional
    private void transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        withdraw(amount, fromAccount.getIban());
        deposit(amount, toAccount.getIban());
    }


    @Transactional
    public int updateBankAccount(BankAccountRequestCurrencyChange bankAccountRequestCurrencyChange, String userName) {
        int updatedRows = 0;
        if (!bankAccountRequestCurrencyChange.toCurrency().isEmpty() && !bankAccountRequestCurrencyChange.fromCurrency().isEmpty()
                && !bankAccountRequestCurrencyChange.fromCurrency().equalsIgnoreCase(bankAccountRequestCurrencyChange.toCurrency())) {
            String iban = bankAccountRequestCurrencyChange.iban();
            BankAccount bankAccount = bankAccountRepository.findByIbanEquals(iban);
            if (bankAccount.getBankAccountStatus() == BankAccountStatus.ACTIVE) {
                double balance = bankAccount.getBalance();
                if (bankAccountRequestCurrencyChange.toCurrency().equalsIgnoreCase("EUR")) {
                    balance = 0.51 * balance;
                } else {
                    balance = 1.96 * balance;
                }
                User user = userRepository.findByUserName(userName);
                Long userId = user.getId();
                updatedRows += updateBankAccountBalance(balance, userId);
                updatedRows += updateBankAccountCurrency(Currency.getCurrencyFromString(bankAccountRequestCurrencyChange.toCurrency()), userId);
                logger.info("Request to BankAccountService: update IBAN: " + iban
                + " to currency: " + bankAccountRequestCurrencyChange.toCurrency());
                bankAccountRepository.save(bankAccount);
                // send mail
                return updatedRows;
            } else {
                AccessDenied accessDenied = new AccessDenied("Bank account with IBAN: " + iban + " is INACTIVE!");
                logger.error("Bank account with IBAN: {} is INACTIVE!", iban, accessDenied);
                throw accessDenied;
            }
        } else {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Invalid input data for bankAccount currency change!");
            logger.error("Invalid input data for bankAccount currency change!");
            throw illegalArgumentException;
        }
    }
}