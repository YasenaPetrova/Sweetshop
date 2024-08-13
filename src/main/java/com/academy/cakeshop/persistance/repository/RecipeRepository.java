package com.academy.cakeshop.persistance.repository;

import com.academy.cakeshop.persistance.entity.Recipe;
import com.academy.cakeshop.persistance.entity.RecipeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeProduct, Long> {
    @Query("SELECT rp FROM RecipeProduct rp WHERE rp.recipe.id = :articleId")
    Optional<RecipeProduct> findByArticleId(@Param("articleId") Long articleId);
}
