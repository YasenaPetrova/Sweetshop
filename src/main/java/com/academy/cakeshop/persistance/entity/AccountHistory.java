package com.academy.cakeshop.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Table(name = "account_history")
public class AccountHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId", nullable = false,unique = true)
    private Long id;

    @NotNull(message = "Date is mandatory")
    @Column(name = "date", nullable = false)
    @JdbcTypeCode(SqlTypes.DATE)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="fromAccount", referencedColumnName = "iban")
    private BankAccount fromAccount;

    @ManyToOne
    @JoinColumn(name="toAccount", referencedColumnName = "iban")
    private BankAccount toAccount;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    @Column(name="amount", nullable=false)
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private double amount;

    @NotBlank(message = "Currency is mandatory")
    @Column(name="currency", length=255, nullable=false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String currency;

//    @ManyToOne
//    @JoinColumn(name="toUser", referencedColumnName = "userId")
//    private User toUser;

}