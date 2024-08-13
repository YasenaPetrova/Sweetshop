package com.academy.cakeshop.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class RecipeProductId implements Serializable {
    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeProductId that = (RecipeProductId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, recipeId);
    }
}