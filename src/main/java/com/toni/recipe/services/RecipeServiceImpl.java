package com.toni.recipe.services;

import com.toni.recipe.commands.RecipeCommand;
import com.toni.recipe.converters.RecipeCommandToRecipe;
import com.toni.recipe.converters.RecipeToRecipeCommand;
import com.toni.recipe.domain.Recipe;
import com.toni.recipe.exceptions.NotFoundException;
import com.toni.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("I'm in the service");

        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(String l) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(l);
        if (!recipeOptional.isPresent()) {
            throw new NotFoundException("Recipe not found for id value: "+l.toString());
        }
        return recipeOptional.get();
    }

    @Override
    public void deleteById(String id){
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(String id){
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }
}