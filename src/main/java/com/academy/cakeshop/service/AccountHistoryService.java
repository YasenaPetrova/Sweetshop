package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.AccountHistoryRequestDTO;
import com.academy.cakeshop.persistance.entity.AccountHistory;
import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.persistance.repository.AccountHistoryRepository;
import com.academy.cakeshop.persistance.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class AccountHistoryService {

    private static final Logger log = Logger.getLogger(AccountHistoryService.class.getName());

    private final AccountHistoryRepository accountHistoryRepository;
    private final BankAccountRepository bankAccountRepository;

    public AccountHistory createAccountHistory(AccountHistoryRequestDTO accountHistoryRequestDTO) {
        log.info("Creating account history");

        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setDate(accountHistoryRequestDTO.date());

        BankAccount fromAccount = bankAccountRepository.findByIbanEquals(accountHistoryRequestDTO.fromAccount());
        BankAccount toAccount = bankAccountRepository.findByIbanEquals(accountHistoryRequestDTO.toAccount());

        accountHistory.setFromAccount(fromAccount);
        accountHistory.setToAccount(toAccount);
        accountHistory.setAmount(accountHistoryRequestDTO.amount());
        accountHistory.setCurrency(accountHistoryRequestDTO.currency());

        AccountHistory savedHistory = accountHistoryRepository.save(accountHistory);
        log.info("Account history created with ID: " + savedHistory.getId());

        return savedHistory;
    }

    public List<AccountHistory> getAllAccountHistories() {
        log.info("Fetching all account histories");
        return accountHistoryRepository.findAll();
    }

    public Optional<AccountHistory> getAccountHistoryById(Long id) {
        log.info("Fetching account history by ID: " + id);
        return accountHistoryRepository.findById(id);
    }

    public AccountHistory updateAccountHistory(Long id, AccountHistoryRequestDTO accountHistoryRequestDTO) {
        log.info("Updating account history with ID: " + id);

        Optional<AccountHistory> accountHistoryOptional = accountHistoryRepository.findById(id);
        if (accountHistoryOptional.isPresent()) {
            AccountHistory accountHistory = accountHistoryOptional.get();
            accountHistory.setDate(accountHistoryRequestDTO.date());

            BankAccount fromAccount = bankAccountRepository.findByIbanEquals(accountHistoryRequestDTO.fromAccount());
            BankAccount toAccount = bankAccountRepository.findByIbanEquals(accountHistoryRequestDTO.toAccount());

            accountHistory.setFromAccount(fromAccount);
            accountHistory.setToAccount(toAccount);
            accountHistory.setAmount(accountHistoryRequestDTO.amount());
            accountHistory.setCurrency(accountHistoryRequestDTO.currency());

            AccountHistory updatedHistory = accountHistoryRepository.save(accountHistory);
            log.info("Account history updated with ID: " + updatedHistory.getId());

            return updatedHistory;
        } else {
            log.warning("Account history not found for ID: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHistory not found");
        }
    }

    public void deleteAccountHistory(Long id) {
        log.info("Deleting account history with ID: " + id);

        Optional<AccountHistory> accountHistoryOptional = accountHistoryRepository.findById(id);
        if (accountHistoryOptional.isPresent()) {
            accountHistoryRepository.deleteById(id);
            log.info("Account history deleted successfully");
        } else {
            log.warning("Account history not found for ID: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHistory not found");
        }
    }
}
