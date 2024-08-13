package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.RentDTO;
import com.academy.cakeshop.dto.SaleResponseDTO;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.errorHandling.InsufficientFundsException;
import com.academy.cakeshop.persistance.entity.AccountHistory;
import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.persistance.entity.Sale;
import com.academy.cakeshop.persistance.repository.AccountHistoryRepository;
import com.academy.cakeshop.persistance.repository.BankAccountRepository;
import com.academy.cakeshop.persistance.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.Lombok;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentService {

    private final BankAccountRepository bankAccountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final SaleRepository saleRepository;

    public RentService(BankAccountRepository bankAccountRepository, AccountHistoryRepository accountHistoryRepository, SaleRepository saleRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountHistoryRepository = accountHistoryRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional
    public void distributeIncome(LocalDate saleDate, double rentAmount) {
        String storeAccountEURIBAN = "DE75512108001245126199";
        String storeAccountBGNIBAN = "GB33BUKB20201555555555";
        BankAccount storeAccountEUR = bankAccountRepository.findByIbanEquals(storeAccountEURIBAN);
        if (storeAccountEUR == null || storeAccountEUR.getBankAccountStatus() != BankAccountStatus.ACTIVE) {
            throw new BusinessNotFound("Store account 1 not found or inactive");
        }

        BankAccount storeAccountBGN = bankAccountRepository.findByIbanEquals(storeAccountBGNIBAN);
        if (storeAccountBGN == null || storeAccountBGN.getBankAccountStatus() != BankAccountStatus.ACTIVE) {
            throw new BusinessNotFound("Store account 2 not found or inactive");
        }

        List<SaleResponseDTO> salesDTO = saleRepository.findBySaleDate(saleDate);
        double totalSalesAmount = salesDTO.stream().mapToDouble(SaleResponseDTO::amount).sum();

        double rentPortion = totalSalesAmount * 0.30; // 30% за наем
        double store2Portion = totalSalesAmount * 0.70; // 70% за сметка 2

        double rentPortionInEUR = convertToEuro(rentPortion); // конвертираме rentPortion в Евро

        if (storeAccountEUR.getBalance() + rentPortionInEUR <= rentAmount) {
            storeAccountEUR.setBalance(storeAccountEUR.getBalance() + rentPortionInEUR);
            accountHistoryRepository.save(createAccountHistory(storeAccountEUR, rentPortionInEUR, "Rent portion", saleDate));
        } else {
            //add email
            throw new InsufficientFundsException("Rent portion exceeds allowed rent amount");

        }

        storeAccountBGN.setBalance(storeAccountBGN.getBalance() + store2Portion);
        accountHistoryRepository.save(createAccountHistory(storeAccountBGN, store2Portion, "Store income", saleDate));

        bankAccountRepository.save(storeAccountEUR);
        bankAccountRepository.save(storeAccountBGN);
    }

    @Transactional
    public void payRent(RentDTO rentDTO) {
        String storeAccountEURIBAN = "DE75512108001245126199";
        Long mallAccountId = rentDTO.mallAccountId();
        Double rentAmount = rentDTO.rentAmount();
        LocalDate dueDate = rentDTO.dueDate();

        BankAccount storeAccount = bankAccountRepository.findByIbanEquals(storeAccountEURIBAN);
        BankAccount mallAccount = bankAccountRepository.getReferenceById(mallAccountId);

        // Проверка за достатъчност на средства и дата на плащане
        if (storeAccount.getBalance() >= rentAmount && LocalDate.now().isAfter(dueDate)) {
            storeAccount.setBalance(storeAccount.getBalance() - rentAmount);
            if (mallAccount.getBankAccountStatus() == BankAccountStatus.ACTIVE) {
                mallAccount.setBalance(mallAccount.getBalance() + rentAmount);
            }

            // Записване на историята на сметките
            accountHistoryRepository.save(createAccountHistory(storeAccount, rentAmount, "Плащане на наем", LocalDate.now()));
            accountHistoryRepository.save(createAccountHistory(mallAccount, rentAmount, "Получен наем", LocalDate.now()));
        } else {
            throw new InsufficientFundsException("Недостатъчни средства или плащане преди падежа");
        }

        bankAccountRepository.save(storeAccount);
        bankAccountRepository.save(mallAccount);
    }

    private double convertToEuro(double amount) {
        double conversionRate = 1.96; // Конверсионен курс
        return amount * conversionRate;
    }

    private AccountHistory createAccountHistory(BankAccount account, double amount, String description, LocalDate date) {
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setAmount(amount);
        accountHistory.setCurrency(account.getCurrency().toString());
        accountHistory.setDate(date);
        return accountHistory;
    }
}