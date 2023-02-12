package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.RecipeDtoAssembler;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.RecipeDto;
import com.epam.esm.giftcertificates.handler.exception.RecipeTotalCostCalculationException;
import com.epam.esm.giftcertificates.repository.RecipeRepository;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Recipe;
import com.epam.esm.giftcertificates.handler.exception.RecipeNotFoundException;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.RecipeService;
import com.epam.esm.giftcertificates.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

  private final GiftCertificateService giftCertificateService;
  private final PersonService personService;
  private final RecipeDtoAssembler recipeDtoAssembler;
  private final PagedResourcesAssembler<Recipe> pagedResourcesAssembler;
  private final RecipeRepository recipeRepository;

  @Override
  @Transactional
  public EntityModel<RecipeDto> createRecipe(Long id, RecipeDto recipeDto) {
    var recipe = new Recipe();
    var giftCertificates =
        recipeDto.getGiftCertificates().stream()
            .map(GiftCertificateDto::getId)
            .map(giftCertificateService::getGiftCertificateById)
            .toList();
    recipe.setGiftCertificates(giftCertificates);
    recipe.setTotalCost(
        giftCertificates.stream()
            .map(GiftCertificate::getPrice)
            .reduce(BigDecimal::add)
            .orElseThrow(RecipeTotalCostCalculationException::new));
    recipe.setPerson(personService.getPersonById(id));
    recipeRepository.save(recipe);
    return EntityModel.of(recipeDtoAssembler.toModel(recipe));
  }

  @Override
  public PagedModel<RecipeDto> getAllRecipes(Long id, int page, int size) {
    return pagedResourcesAssembler.toModel(
        recipeRepository.getAllByPersonId(id, PageRequest.of(page, size)), recipeDtoAssembler);
  }

  @Override
  public EntityModel<RecipeDto> getRecipeById(Long id) {
    return EntityModel.of(
        recipeDtoAssembler.toModel(
            recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id))));
  }
}
