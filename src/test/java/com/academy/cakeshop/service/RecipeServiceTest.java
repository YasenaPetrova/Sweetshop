package com.academy.cakeshop.service;

import com.academy.cakeshop.persistance.entity.Recipe;
import com.academy.cakeshop.persistance.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RecipeServiceTest {

    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository = Mockito.mock(RecipeRepository.class);
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    void createRecipe() {
        Recipe recipe = new Recipe(5L, "Chocolate Cake", null, null);
        Mockito.when(recipeRepository.save(recipe)).thenReturn(recipe);
        Recipe createdRecipe = recipeService.createRecipe(recipe);
        assertEquals(recipe, createdRecipe);
    }

    @Test
    void givenValidID_WhenGettingRecipeByID_ThenCorrectRecipeReturned() {
        Long id = 5L;
        Recipe expectedRecipe = new Recipe(5L, "Chocolate Cake", null, null);
        Mockito.when(recipeRepository.findById(id)).thenReturn(Optional.of(expectedRecipe));
        Optional<Recipe> actualRecipe = recipeService.getRecipeById(id);
        assertTrue(actualRecipe.isPresent());
        assertEquals(expectedRecipe, actualRecipe.get(), "Recipes don't match");
    }

    @Test
    void givenInvalidID_WhenGettingRecipeByID_ThenThrowException() {
        Long id = 5L;
        Mockito.when(recipeRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(id));
        String expectedMessage = "Recipe not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getAllRecipes() {
        Recipe expectedRecipe1 = new Recipe(5L, "Chocolate Cake", null, null);
        Recipe expectedRecipe2 = new Recipe(10L, "Vanilla Cake", null, null);
        List<Recipe> expectedList = new ArrayList<>();
        expectedList.add(expectedRecipe1);
        expectedList.add(expectedRecipe2);

        Mockito.when(recipeRepository.findAll()).thenReturn(expectedList);
        List<Recipe> actualList = recipeService.getAllRecipes();
        assertEquals(expectedList, actualList, "Lists don't match");
    }

    @Test
    void updateRecipe() {
        Long id = 5L;
        Recipe existingRecipe = new Recipe(id, "Chocolate Cake", null, null);
        Recipe updatedDetails = new Recipe(id, "Strawberry Cake", null, null);

        Mockito.when(recipeRepository.findById(id)).thenReturn(Optional.of(existingRecipe));
        Mockito.when(recipeRepository.save(existingRecipe)).thenReturn(updatedDetails);

        Recipe updatedRecipe = recipeService.updateRecipe(id, updatedDetails);
        assertEquals(updatedDetails.getName(), updatedRecipe.getName());
    }

    @Test
    void givenInvalidID_WhenUpdatingRecipe_ThenThrowException() {
        Long id = 5L;
        Recipe updatedDetails = new Recipe(id, "Strawberry Cake", null, null);

        Mockito.when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.updateRecipe(id, updatedDetails));
        String expectedMessage = "Recipe not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteRecipe() {
        Long id = 5L;
        Mockito.doNothing().when(recipeRepository).deleteById(id);

        assertDoesNotThrow(() -> recipeService.deleteRecipe(id));
        Mockito.verify(recipeRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void givenInvalidID_WhenDeletingRecipe_ThenThrowException() {
        Long id = 5L;
        Mockito.doThrow(new ResourceNotFoundException("Recipe not found")).when(recipeRepository).deleteById(id);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.deleteRecipe(id));
        String expectedMessage = "Recipe not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
