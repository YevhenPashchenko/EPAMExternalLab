package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.dto.RecipeDto;
import com.epam.esm.giftcertificates.service.RecipeService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class RecipeController {

  private final RecipeService recipeService;

  @PostMapping(
      value = "/{id}/recipes",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public EntityModel<RecipeDto> create(
      @PathVariable("id") Long id, @RequestBody RecipeDto recipeDto) {
    return recipeService.createRecipe(id, recipeDto);
  }

  @GetMapping(value = "/{id}/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedModel<RecipeDto> getAll(
      @PathVariable("id") Long id,
      @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0")
          int page,
      @RequestParam(defaultValue = "20")
          @Range(min = 1, max = 100, message = "size must be between 1 and 100")
          int size) {
    return recipeService.getAllRecipes(id, page, size);
  }

  @GetMapping(value = "/recipes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public EntityModel<RecipeDto> getById(@PathVariable("id") Long id) {
    return recipeService.getRecipeById(id);
  }
}
