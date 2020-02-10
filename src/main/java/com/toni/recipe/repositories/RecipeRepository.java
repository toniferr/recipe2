package com.toni.recipe.repositories;

import com.toni.recipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe,String> {

    Optional<Recipe> findById(String id);
}
