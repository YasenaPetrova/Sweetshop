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

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articleId", nullable = false)
    private Long id;

    @NotBlank(message = "Article name is mandatory")
    @Column(name="articleName", length=255, nullable=false, unique=false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String articleName;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be positive")
    @Column(name="price", nullable=false,unique = false)
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private double price;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

}