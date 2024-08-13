package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.*;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.persistance.entity.*;
import com.academy.cakeshop.persistance.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentSupplierService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final SaleRepository saleRepository;
    private final PurchaseOrderService purchaseOrderService;
    private final PaymentRepository paymentRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public void processDailyPayments() {
        logger.info("Starting daily payment processing...");

        LocalDate today = LocalDate.now();


        List<SaleResponseDTO> sales = saleRepository.findBySaleDate(today);
        logger.info("Sales for {}: {}", today, sales);

        double totalSalesAmount = sales.stream().mapToDouble(SaleResponseDTO::amount).sum();
        double percentageSales = totalSalesAmount * 0.3;


        List<PurchaseOrderRequestDTO> purchaseOrders = purchaseOrderService.getPurchaseOrdersByDate(today);
        logger.info("Purchase Orders for {}: {}", today, purchaseOrders);

        for (PurchaseOrderRequestDTO order : purchaseOrders) {
            List<Payment> payments = paymentRepository.findByContractID(order.contractId());
            logger.info("Payments for Contract ID {}: {}", order.contractId(), payments);

            if (payments.isEmpty()) {
                processPayment(order, percentageSales);
                logger.info("Processed payment for Purchase Order ID {} with amount {}", order.contractId(), percentageSales);
            }
        }

        logger.info("Daily payment processing completed.");
    }
    @Transactional
    public void processPayment(PurchaseOrderRequestDTO order, double availableAmount) {
        Contract contract = contractRepository.findById(order.contractId()).orElseThrow();
        BankAccount fromAccount = bankAccountRepository.findByIbanEquals("GB33BUKB20201555555555");

        if (fromAccount != null) {
            BankAccount toAccount = bankAccountRepository.findByUserID(contract.getUser().getId());

            if (toAccount.getBankAccountStatus() == BankAccountStatus.ACTIVE && availableAmount >= order.price()) {
                double newBalance = fromAccount.getBalance() - order.price();
                bankAccountRepository.updateBalanceBy(newBalance, fromAccount.getUser().getId());

                double newSupplierBalance = toAccount.getBalance() + order.price();
                bankAccountRepository.updateBalanceBy(newSupplierBalance, toAccount.getUser().getId());

                Payment payment = new Payment();
                payment.setDate(LocalDate.now());
                payment.setContract(contract);
                payment.setFromBankAccount(fromAccount);
                payment.setToBankAccount(toAccount);
                payment.setAmount(order.price());
                payment.setCurrency(contract.getCurrency());
                paymentRepository.save(payment);

                AccountHistory accountHistory = new AccountHistory();
                accountHistory.setDate(LocalDate.now());
                accountHistory.setFromAccount(fromAccount);
                accountHistory.setToAccount(toAccount);
                accountHistory.setAmount(order.price());
                accountHistory.setCurrency(contract.getCurrency().toString());
                accountHistoryRepository.save(accountHistory);

                logger.info("Processed payment for order ID: " + order.contractId());
            } else {
                logger.warn("Insufficient funds or inactive account for order ID: " + order.contractId());
            }
        } else {
            logger.warn("Bank account with IBAN 'SOME_IBAN' not found");
        }
    }

    @Transactional
    public void processAdvancePaymentRequest(PurchaseOrderRequestDTO order) {
        List<Payment> payments = paymentRepository.findByContractID(order.contractId());
        if (payments.isEmpty()) {
            processPayment(order, Double.MAX_VALUE);
        } else {
            logger.warn("Payment already processed for order ID: " + order.contractId());
        }
    }
}