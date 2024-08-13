//package com.academy.cakeshop.service;
//
//import com.academy.cakeshop.dto.RecipeDTO;
//import com.academy.cakeshop.errorHandling.BusinessNotFound;
//import com.academy.cakeshop.persistance.entity.Recipe;
//import com.academy.cakeshop.persistance.repository.RecipeRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class RecipeService {
//    private RecipeRepository recipeRepository;
//
//    public RecipeService(RecipeRepository recipeRepository) {
//        this.recipeRepository = recipeRepository;
//    }
//
//    public Recipe createRecipe(Recipe recipe) {
//        return recipeRepository.save(recipe);
//    }
//
//    public Recipe getRecipeById(Long id) {
//        Recipe recipe = recipeRepository.getReferenceById(id);
//        if (recipe != null){
//            return  recipe;
//        }else {
//            throw new BusinessNotFound("Recipe ID does not exist!");
//        }
//    }
//
//    public List<Recipe> getAllRecipes() {
//        return recipeRepository.findAll();
//    }
//
//    public Recipe updateRecipe(Long id, RecipeDTO recipeDetails) {
//        Recipe recipe = recipeRepository.getReferenceById(id);
//        if (recipe != null){
//            recipe.setName(recipeDetails.name());
//            return recipeRepository.save(recipe);
//        }else{
//            throw new BusinessNotFound("No Recipe with ID: " + id + " Found!");
//        }
//    }
//
//    public void deleteRecipe(Long id) {
//        recipeRepository.deleteById(id);
//    }
//}
