package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.*;
import com.academy.cakeshop.enumeration.ContractStatus;
import com.academy.cakeshop.enumeration.Currency;
import com.academy.cakeshop.enumeration.Role;
import com.academy.cakeshop.persistance.entity.*;
import com.academy.cakeshop.persistance.repository.*;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SaleService {


    private final SaleRepository saleRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final BankAccountRepository bankAccountRepository;

    public Sale createSale(SaleRequestDTO saleRequestDTO) {
        Sale sale = new Sale();
        sale.setDate(saleRequestDTO.date());
        sale.setAmount(saleRequestDTO.amount());
        Optional<Article> articleOptional = articleRepository.findById(saleRequestDTO.articleId());
        if (articleOptional.isPresent()) {
            sale.setArticle(articleOptional.get());
            return saleRepository.save(sale);
        } else {
            throw new RuntimeException("Sale not found");
        }
    }

    public List<SaleResponseDTO> getSalesByDate(LocalDate date) {
        return saleRepository.findBySaleDate(date);
    }

    public List<Sale> getListOfSalesByDate(LocalDate date) {
        return saleRepository.findListOfSalesByDate(date);
    }

    public List<Sale> getAllSale() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale updateSale(Long id, SaleRequestDTO saleRequestDTO) {
        Optional<Sale> saleOptional = saleRepository.findById(id);
        if (saleOptional.isPresent()) {
            Sale sale = saleOptional.get();
            sale.setDate(saleRequestDTO.date());
            sale.setAmount(saleRequestDTO.amount());
            Optional<Article> articleOptional = articleRepository.findById(saleRequestDTO.articleId());
            if (articleOptional.isPresent()) {
                sale.setArticle(articleOptional.get());
            } else {
                throw new RuntimeException("Article not found");
            }
            return saleRepository.save(sale);
        } else {
            throw new RuntimeException("Sale not found");
        }
    }

    public void deleteSale(Long id) {
        Optional<Sale> saleOptional = saleRepository.findById(id);
        if (saleOptional.isPresent()) {
            saleRepository.deleteById(id);
        } else {
            throw new RuntimeException("Sale not found");
        }
    }

    // Справка при оборот от предния ден и за колко работни дни ще си плати наема.
    public int getHowManyDaysLeftToPayRent() {
        User mall = userRepository.findByRole(Role.MALL);
        Contract contract = contractRepository.findByUserIdActive(mall.getId(), ContractStatus.APPROVED);
        double rent = contract.getContractSum();

        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Sale> sales = getListOfSalesByDate(yesterday);
        double yesterdayGain = 0.0;
        for (Sale sale : sales) {
            Article article = sale.getArticle();
            yesterdayGain += (sale.getAmount() * article.getPrice());
        }
        double yesterdayGainEUR = BankAccountService.getConvertedAmount(yesterdayGain, Currency.EUR);

        User store = userRepository.findByRole(Role.STORE);
        BankAccount bankAccountEUR = bankAccountRepository.findByUserIdAndCurrency(store.getId(), Currency.EUR);
        double currentGainedRent = bankAccountEUR.getBalance();
        double rentToPay = rent - currentGainedRent;

        logger.info("Request to DB: sales prognosis for rent payment");
        return (int) (rentToPay / yesterdayGainEUR);
    }

    // Мениджъра на магазина прави справка колко еклера и торти тряба да продаде, за да не фалира от наема

    public List<SaleStatDTO> getArticlesToSell() {
        User user = userRepository.findByRole(Role.MALL);
        Contract contract = contractRepository.findByUserIdActive(user.getId(), ContractStatus.APPROVED);
        double rent = contract.getContractSum();

        List<Article> articles = articleRepository.findAll();
        List<SaleStatDTO> saleStatDTOList = new ArrayList<>();

        for (Article article : articles) {
            double price = article.getPrice();
            int count = (int) (rent / price);
            SaleStatDTO saleStatDTO = new SaleStatDTO(article.getArticleName(), article.getPrice(), count);
            saleStatDTOList.add(saleStatDTO);
        }
        logger.info("Request to DB: sales statistics per article");
        return saleStatDTOList;
    }

}