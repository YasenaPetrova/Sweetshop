package com.academy.cakeshop.persistance.repository;

import com.academy.cakeshop.dto.PurchaseOrderRequestDTO;
import com.academy.cakeshop.persistance.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("select po from PurchaseOrder po WHERE po.date = :date")
    List<PurchaseOrderRequestDTO> findByOrderDate(@Param("date") LocalDate date);



    @Query("select p from PurchaseOrder p where p.contract.id = ?1 order by p.date DESC")
    List<PurchaseOrder> findByContractIDOrderByDate(Long id);
}
