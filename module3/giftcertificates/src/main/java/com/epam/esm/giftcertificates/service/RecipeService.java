package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.RecipeDto;
import com.epam.esm.giftcertificates.entity.Person;
import com.epam.esm.giftcertificates.entity.Recipe;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * API provides methods for services {@link Recipe} objects data before CR operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface RecipeService {

  /**
   * Saves {@link Recipe} object.
   *
   * @param id {@link Person} object {@code id}.
   * @param recipeDto {@link RecipeDto} object.
   * @return {@link Recipe} object.
   */
  EntityModel<RecipeDto> createRecipe(Long id, RecipeDto recipeDto);

  /**
   * Returns a list of {@link Person} {@link RecipeDto} objects from given {@code page} and {@code
   * size}.
   *
   * @param id {@link Person} object {@code id}.
   * @param page {@code page}.
   * @param size {@code size}.
   * @return list of {@link RecipeDto} objects.
   */
  PagedModel<RecipeDto> getAllRecipes(Long id, int page, int size);

  /**
   * Returns an {@link RecipeDto} object by {@code id}.
   *
   * @param id object {@code id}.
   * @return {@link RecipeDto} object.
   */
  EntityModel<RecipeDto> getRecipeById(Long id);
}
