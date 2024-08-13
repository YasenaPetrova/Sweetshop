package com.academy.cakeshop.persistance.entity;

import com.academy.cakeshop.enumeration.ContractPeriod;
import com.academy.cakeshop.enumeration.ContractStatus;
import com.academy.cakeshop.enumeration.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "contract_sum", precision = 19, scale = 2)
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private Double contractSum;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_period")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private ContractPeriod contractPeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private ContractStatus contractStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}