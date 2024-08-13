package com.academy.cakeshop.persistance.entity;

import com.academy.cakeshop.enumeration.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "date")
    @JdbcTypeCode(SqlTypes.DATE)
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_source_id")
    private BankAccount fromBankAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_destination_id")
    private BankAccount toBankAccount;

    @Column(name = "amount")
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Currency currency;
}