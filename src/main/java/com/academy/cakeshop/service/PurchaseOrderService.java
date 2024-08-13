package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.dto.PurchaseStatDTO;
import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.enumeration.ContractStatus;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
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
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;

    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setDate(purchaseOrderRequestDTO.date());
        purchaseOrder.setPrice(purchaseOrderRequestDTO.price());
        purchaseOrder.setQuantity(purchaseOrderRequestDTO.quantity());
        purchaseOrder.setBankAccountStatus(BankAccountStatus.valueOf(purchaseOrderRequestDTO.bankAccountStatus()));
        Optional<Product> productOptional = productRepository.findById(purchaseOrderRequestDTO.productId());
        if (productOptional.isPresent()) {
            purchaseOrder.setProduct(productOptional.get());
        } else {
            throw new RuntimeException("Product not found");
        }

        Optional<Unit> unitOptional = unitRepository.findById(purchaseOrderRequestDTO.unitId());
        if (unitOptional.isPresent()) {
            purchaseOrder.setUnit(unitOptional.get());
        } else {
            throw new RuntimeException("Unit not found");
        }

        Optional<Contract> contractOptional = contractRepository.findById(purchaseOrderRequestDTO.contractId());
        if (contractOptional.isPresent()) {
            purchaseOrder.setContract(contractOptional.get());
        } else {
            throw new RuntimeException("Contract not found");
        }

        return purchaseOrderRepository.save(purchaseOrder);
    }
    public List<PurchaseOrder> getAllPurchaseOrder() {
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }
    public List<PurchaseOrderRequestDTO> getPurchaseOrdersByDate(LocalDate date) {
        return purchaseOrderRepository.findByOrderDate(date);
    }
    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        Optional<PurchaseOrder> purchaseOrderOptional = purchaseOrderRepository.findById(id);
        if (purchaseOrderOptional.isPresent()) {
            PurchaseOrder purchaseOrder = purchaseOrderOptional.get();
            purchaseOrder.setDate(purchaseOrderRequestDTO.date());
            purchaseOrder.setPrice(purchaseOrderRequestDTO.price());
            purchaseOrder.setQuantity(purchaseOrderRequestDTO.quantity());
            purchaseOrder.setBankAccountStatus(BankAccountStatus.valueOf(purchaseOrderRequestDTO.status()));
            Optional<Product> productOptional = productRepository.findById(purchaseOrderRequestDTO.productId());
            productOptional.ifPresent(purchaseOrder::setProduct);
            Optional<Unit> unitOptional = unitRepository.findById(purchaseOrderRequestDTO.unitId());
            unitOptional.ifPresent(purchaseOrder::setUnit);
            Optional<Contract> contractOptional = contractRepository.findById(purchaseOrderRequestDTO.contractId());
            contractOptional.ifPresent(purchaseOrder::setContract);

            return purchaseOrderRepository.save(purchaseOrder);
        } else {
            throw new RuntimeException("Purchase order not found");
        }
    }
            public void deletePurchaseOrder(Long id) {
        Optional<PurchaseOrder> purchaseOrderOptional = purchaseOrderRepository.findById(id);
        if (purchaseOrderOptional.isPresent()) {
            purchaseOrderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Purchase order not found");
        }
    }

    // Всеки от търговците има достъп до статистика за оборота по дни, печалба и наличности.

    public List<PurchaseStatDTO> getSupplierPurchaseStatistics(String userName) {
        User user = userRepository.findByUserName(userName);
        Contract contract = contractRepository.findByUserIdActive(user.getId(), ContractStatus.APPROVED);
        if (contract != null) {
            List<PurchaseOrder> orders = purchaseOrderRepository.findByContractIDOrderByDate(contract.getId());
            List<PurchaseStatDTO> purchaseStatDTOList = new ArrayList<>();

            for (PurchaseOrder order : orders) {
                Product product = order.getProduct();
                Unit unit = product.getUnit();
                double gain = product.getPricePerUnit() * order.getQuantity();
                PurchaseStatDTO purchaseStatDTO = new PurchaseStatDTO(order.getDate(), contract.getId(),
                        contract.getContractStatus().toString(), product.getName(), order.getQuantity(), unit.getName(),
                        order.getPrice(), gain);
                purchaseStatDTOList.add(purchaseStatDTO);
            }
            logger.info("Request to DB: purchase order statistics for userName" + userName);
            return purchaseStatDTOList;
        } else {
            BusinessNotFound businessNotFound = new BusinessNotFound("No active contracts found for userName: " + userName);
            logger.error("Error: No active contracts found for userName: {}", userName, businessNotFound);
            throw businessNotFound;
        }
    }
}