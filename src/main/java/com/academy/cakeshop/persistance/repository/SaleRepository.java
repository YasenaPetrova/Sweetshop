package com.academy.cakeshop.persistance.repository;

import com.academy.cakeshop.dto.SaleResponseDTO;
import com.academy.cakeshop.persistance.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT new com.academy.cakeshop.dto.SaleResponseDTO(s.date, s.amount, s.article.id) FROM Sale s WHERE s.date = :date")
    List<SaleResponseDTO> findBySaleDate(@Param("date") LocalDate date);

    @Query("select s from Sale s where s.date = ?1")
    List<Sale> findListOfSalesByDate(LocalDate date);
}
