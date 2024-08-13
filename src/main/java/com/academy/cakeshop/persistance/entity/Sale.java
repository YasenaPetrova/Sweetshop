package com.academy.cakeshop.persistance.entity;

import com.academy.cakeshop.persistance.entity.Article;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Date is mandatory")
    @Column(name = "date", nullable = false)
    @JdbcTypeCode(SqlTypes.DATE)
    private LocalDate date;


    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    @Column(name="amount", nullable=false)
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private double amount;

    @ManyToOne
    @JoinColumn(name="articleId",referencedColumnName = "articleId")
    private Article article;
}