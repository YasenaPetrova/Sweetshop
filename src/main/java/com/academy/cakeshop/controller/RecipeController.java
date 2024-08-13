//package com.academy.cakeshop.controller;
//
//import com.academy.cakeshop.dto.RecipeDTO;
//import com.academy.cakeshop.errorHandling.BusinessNotFound;
//import com.academy.cakeshop.persistance.entity.Recipe;
//import com.academy.cakeshop.service.RecipeService;
//import jakarta.validation.Valid;
//import org.springframework.data.rest.webmvc.ResourceNotFoundException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/api/recipes")
//public class RecipeController {
//    private RecipeService recipeService;
//
//    public RecipeController(RecipeService recipeService) {
//        this.recipeService = recipeService;
//    }
//
//    @PostMapping
//    public Recipe createRecipe(@Valid @RequestBody Recipe recipe) {
//        Recipe createdRecipe = recipeService.createRecipe(recipe);
//        return ResponseEntity.ok(createdRecipe).getBody();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
//        try {
//            Recipe recipe = recipeService.getRecipeById(id);
//            return ResponseEntity.ok(recipe);
//        } catch (BusinessNotFound e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping
//    public List<Recipe> getAllRecipes() {
//        List<Recipe> recipes = recipeService.getAllRecipes();
//        return ResponseEntity.ok(recipes).getBody();
//
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDetails) {
//        try {
//            Recipe updatedRecipe = recipeService.updateRecipe(id, recipeDetails);
//            return ResponseEntity.ok(updatedRecipe);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
//        recipeService.deleteRecipe(id);
//        return ResponseEntity.noContent().build();
//    }
//}