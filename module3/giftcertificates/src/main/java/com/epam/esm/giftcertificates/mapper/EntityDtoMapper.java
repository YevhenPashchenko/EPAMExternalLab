package com.epam.esm.giftcertificates.mapper;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDtoForUpdate;
import com.epam.esm.giftcertificates.dto.RecipeDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Recipe;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.entity.Person;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntityDtoMapper {

  /**
   * Converts {@link GiftCertificateDto} object to {@link GiftCertificate} object.
   *
   * @param giftCertificateDto {@link GiftCertificateDto} object.
   * @return {@link GiftCertificate} object.
   */
  GiftCertificate giftCertificateDtoToGiftCertificate(GiftCertificateDto giftCertificateDto);

  /**
   * Converts {@link GiftCertificate} object to {@link GiftCertificateDto} object.
   *
   * @param giftCertificate {@link GiftCertificate} object.
   * @return {@link GiftCertificateDto} object.
   */
  GiftCertificateDto giftCertificateToGiftCertificateDto(GiftCertificate giftCertificate);

  /**
   * Update {@link GiftCertificate} object fields value from not null {@link GiftCertificateDto}
   * object fields value.
   *
   * @param giftCertificate {@link GiftCertificate} object.
   * @param giftCertificateDtoForUpdate {@link GiftCertificateDtoForUpdate} object.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateGiftCertificateFromGiftCertificateDto(
      @MappingTarget GiftCertificate giftCertificate,
      GiftCertificateDtoForUpdate giftCertificateDtoForUpdate);

  /**
   * Converts {@link TagDto} object to {@link Tag} object.
   *
   * @param tagDto {@link TagDto} object.
   * @return {@link Tag} object.
   */
  Tag tagDtoToTag(TagDto tagDto);

  /**
   * Converts {@link Tag} object to {@link TagDto} object.
   *
   * @param tag {@link Tag} object.
   * @return {@link TagDto} object.
   */
  TagDto tagToTagDto(Tag tag);

  /**
   * Converts {@link Person} object to {@link PersonDto} object.
   *
   * @param person {@link Person} object.
   * @return {@link PersonDto} object.
   */
  PersonDto personToPersonDto(Person person);

  /**
   * Converts {@link Recipe} object to {@link RecipeDto} object.
   *
   * @param recipe {@link Recipe} object.
   * @return {@link RecipeDto} object.
   */
  RecipeDto recipeToRecipeDto(Recipe recipe);
}
