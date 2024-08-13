package com.academy.cakeshop.persistance.entity;

import com.academy.cakeshop.enumeration.BankAccountStatus;
import com.academy.cakeshop.enumeration.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "iban", unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String iban;

    @Column(name = "balance")
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private Double balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_account_status")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private BankAccountStatus bankAccountStatus;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;
}