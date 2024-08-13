package com.academy.cakeshop.persistance.repository;

import com.academy.cakeshop.persistance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.contract.id = ?1")
    List<Payment> findByContractID(Long id);
}