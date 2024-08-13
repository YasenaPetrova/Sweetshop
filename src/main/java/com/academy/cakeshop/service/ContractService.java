package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.ContractRequest;
import com.academy.cakeshop.dto.ContractResponse;
import com.academy.cakeshop.enumeration.ContractPeriod;
import com.academy.cakeshop.enumeration.ContractStatus;
import com.academy.cakeshop.enumeration.Currency;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.Contract;
import com.academy.cakeshop.persistance.entity.User;
import com.academy.cakeshop.persistance.repository.ContractRepository;
import com.academy.cakeshop.persistance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public ContractService(ContractRepository contractRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    public ContractResponse getByID(Long id) {
        Contract contract = contractRepository.getReferenceById(id);
        if (contract != null) {
            String contractorName = contract.getUser().getFirstName() + " " + contract.getUser().getLastName();
            logger.info("Request to DB: getContract by id: " + id);
            return new ContractResponse(contract.getContractSum(), contract.getCurrency().toString(),
                    contract.getContractPeriod().toString(), contractorName);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No contract with id: " + id + "found!");
            logger.error("Error: No contract with id {} found", id, businessNotFound);
            throw businessNotFound;
        }
    }

//    public List<Contract> getAll() {
//        return contractRepository.findAll();
//    }

    public List<ContractResponse> getByUserID(long userId) {
        List<Contract> contracts = contractRepository.findAllByUserId(userId);
        List<ContractResponse> contractResponseList = new ArrayList<>();
        if (contracts != null) {
            for (Contract contract : contracts) {
                String contractorName = contract.getUser().getFirstName() + " " + contract.getUser().getLastName();
                ContractResponse contractResponse = new ContractResponse(contract.getContractSum(),
                        contract.getCurrency().toString(), contract.getContractPeriod().toString(), contractorName);
                contractResponseList.add(contractResponse);
            }
            logger.info("Request to DB: getContracts by userId: " + userId);
            return contractResponseList;
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No contracts with userId: " + userId + "found!");
            logger.error("Error: No contracts with userId {} found", userId, businessNotFound);
            throw businessNotFound;
        }
    }

    public boolean existsByID(Long id) {
        logger.info("Request to ContractService: check for existing contractId: " + id);
        return contractRepository.existsById(id);
    }

    public void create(ContractRequest contractRequest, String userName) {
        Contract contract = new Contract();
        contract.setContractSum(contractRequest.contractSum());
        contract.setCurrency(Currency.getCurrencyFromString(contractRequest.currency()));
        contract.setContractPeriod(ContractPeriod.getContractPeriodFromString(contractRequest.contractPeriod()));
        contract.setContractStatus(ContractStatus.NEW);

        User user = userRepository.findByUserName(userName);
        contract.setUser(user);
        contractRepository.saveAndFlush(contract);
        logger.info("Request to DB: create new contract for userName: " + userName
                + " for amount: " + contractRequest.contractSum()
                + " due: " + contractRequest.contractPeriod());
    }

    public int updateContractStatus(String status, Long contractId) {
        ContractStatus contractStatus = ContractStatus.getContractStatusFromString(status);
        logger.info("Request to DB: update Contract Status for contractId: " + contractId);
        return contractRepository.updateContractStatusById(contractStatus, contractId);
    }

    public void deleteById(Long id) {
        if (existsByID(id)) {
            contractRepository.deleteById(id);
            logger.info("Request to DB: delete contract with id: " + id);
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No contract with id: " + id + "found!");
            logger.error("Error: No contract with id {} found", id, businessNotFound);
            throw businessNotFound;
        }
    }
}