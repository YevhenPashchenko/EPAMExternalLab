package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.RecipeDtoAssembler;
import com.epam.esm.giftcertificates.dto.RecipeDto;
import com.epam.esm.giftcertificates.entity.Recipe;
import com.epam.esm.giftcertificates.repository.RecipeRepository;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.handler.exception.RecipeNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.RecipeTotalCostCalculationException;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.RecipeService;
import com.epam.esm.giftcertificates.service.PersonService;
import com.epam.esm.giftcertificates.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class RecipeServiceImplTest {

  private final GiftCertificateService giftCertificateService = mock(GiftCertificateService.class);
  private final PersonService personService = mock(PersonService.class);
  private final RecipeDtoAssembler recipeDtoAssembler = mock(RecipeDtoAssembler.class);
  private final PagedResourcesAssembler<Recipe> pagedResourcesAssembler =
      mock(PagedResourcesAssembler.class);
  private final RecipeRepository recipeRepository = mock(RecipeRepository.class);
  private final RecipeService recipeService =
      new RecipeServiceImpl(
          giftCertificateService, personService, recipeDtoAssembler, pagedResourcesAssembler, recipeRepository);

  @Test
  void createRecipe_shouldReturnRecipeDtoEntityModel_whenExecutedNormally() {
    // GIVEN
    var recipeDto = new RecipeDto();
    recipeDto.setGiftCertificates(List.of(new GiftCertificateDto()));

    var giftCertificate = new GiftCertificate();
    giftCertificate.setPrice(BigDecimal.ONE);

    given(giftCertificateService.getGiftCertificateById(null)).willReturn(giftCertificate);
    given(recipeDtoAssembler.toModel(any(Recipe.class))).willReturn(new RecipeDto());

    // WHEN
    var result = recipeService.createRecipe(0L, recipeDto);

    // THEN
    assertEquals(EntityModel.of(new RecipeDto()), result);
  }

  @Test
  void
      createRecipe_shouldThrowRecipeTotalCostCalculationException_whenGiftCertificatePriceIncorrect() {
    // THEN
    assertThrows(
        RecipeTotalCostCalculationException.class,
        () -> recipeService.createRecipe(0L, new RecipeDto()));
  }

  @Test
  void getAllOrders_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // WHEN
    recipeService.getAllRecipes(0L, 0, 2);

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(recipeRepository.getAllByPersonId(0L, PageRequest.of(0, 2)), recipeDtoAssembler);
  }

  @Test
  void getRecipeById_shouldReturnRecipeDtoEntityModel_whenRecipeWithThisIdExist() {
    // GIVEN
    given(recipeRepository.findById(anyLong())).willReturn(Optional.of(new Recipe()));
    given(recipeDtoAssembler.toModel(any(Recipe.class))).willReturn(new RecipeDto());

    // WHEN
    var result = recipeService.getRecipeById(0L);

    // THEN
    assertEquals(EntityModel.of(new RecipeDto()), result);
  }

  @Test
  void getRecipeById_shouldThrowRecipeNotFoundException_whenRecipeWithThisIdNotExist() {
    // GIVEN
    given(recipeRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(0L));
  }
}
