package com.epam.esm.giftcertificates.assembler;

import com.epam.esm.giftcertificates.controller.GiftCertificateController;
import com.epam.esm.giftcertificates.controller.RecipeController;
import com.epam.esm.giftcertificates.controller.TagController;
import com.epam.esm.giftcertificates.controller.PersonController;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.RecipeDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.entity.Recipe;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RecipeDtoAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeDto> {

  private final EntityDtoMapper entityDtoMapper;

  public RecipeDtoAssembler(EntityDtoMapper entityDtoMapper) {
    super(RecipeController.class, RecipeDto.class);
    this.entityDtoMapper = entityDtoMapper;
  }

  @Override
  public RecipeDto toModel(@NonNull Recipe recipe) {
    var recipeDto = entityDtoMapper.recipeToRecipeDto(recipe);
    recipeDto.setPersonDto(entityDtoMapper.personToPersonDto(recipe.getPerson()));
    recipeDto
        .getGiftCertificates()
        .forEach(
            giftCertificateDto -> {
              giftCertificateDto.getTags().forEach(this::addSelfLinkToTag);
              addSelfLinkToGiftCertificate(giftCertificateDto);
            });
    addSelfLinkToPerson(recipeDto.getPersonDto());
    addSelfLinkToRecipe(recipeDto);
    return recipeDto;
  }

  private void addSelfLinkToTag(TagDto tagDto) {
    tagDto.add(linkTo(methodOn(TagController.class).getById(tagDto.getId())).withSelfRel());
  }

  private void addSelfLinkToGiftCertificate(GiftCertificateDto giftCertificateDto) {
    giftCertificateDto.add(
        linkTo(methodOn(GiftCertificateController.class).getById(giftCertificateDto.getId()))
            .withSelfRel());
  }

  private void addSelfLinkToPerson(PersonDto personDto) {
    personDto.add(
        linkTo(methodOn(PersonController.class).getById(personDto.getId())).withSelfRel());
  }

  private void addSelfLinkToRecipe(RecipeDto recipeDto) {
    recipeDto.add(
        linkTo(methodOn(RecipeController.class).getById(recipeDto.getId())).withSelfRel());
  }
}
