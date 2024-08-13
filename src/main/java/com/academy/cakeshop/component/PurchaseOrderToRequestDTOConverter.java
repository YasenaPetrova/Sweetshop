package com.academy.cakeshop.component;
import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.persistance.entity.PurchaseOrder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderToRequestDTOConverter implements Converter<PurchaseOrder, PurchaseOrderRequestDTO> {
        @Override
        public PurchaseOrderRequestDTO convert(PurchaseOrder purchaseOrder) {
            return new PurchaseOrderRequestDTO(
                    purchaseOrder.getQuantity(),
                    purchaseOrder.getPrice(),
                    purchaseOrder.getDate(),
                    purchaseOrder.getBankAccountStatus().toString(),
                    purchaseOrder.getProduct().getId(),
                    purchaseOrder.getUnit().getId(),
                    purchaseOrder.getBankAccountStatus().toString(),
                    purchaseOrder.getContract().getId()
            );
        }
    }