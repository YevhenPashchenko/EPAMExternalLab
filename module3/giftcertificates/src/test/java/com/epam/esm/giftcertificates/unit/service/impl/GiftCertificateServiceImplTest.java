package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDtoForUpdate;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.handler.exception.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class GiftCertificateServiceImplTest {

  private final GiftCertificateRepository giftCertificateRepository =
      mock(GiftCertificateRepository.class);
  private final TagService tagService = mock(TagService.class);
  private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
  private final GiftCertificateDtoAssembler giftCertificateDtoAssembler =
      mock(GiftCertificateDtoAssembler.class);
  private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler =
      mock(PagedResourcesAssembler.class);
  private final GiftCertificateService giftCertificateService =
      new GiftCertificateServiceImpl(
          giftCertificateRepository,
          tagService,
          entityDtoMapper,
          giftCertificateDtoAssembler,
          pagedResourcesAssembler);

  @Test
  void createGiftCertificate_shouldReturnGiftCertificateDtoEntityModel_whenExecutedNormally() {
    // GIVEN
    given(entityDtoMapper.giftCertificateDtoToGiftCertificate(any(GiftCertificateDto.class)))
        .willReturn(new GiftCertificate());
    given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class)))
        .willReturn(new GiftCertificateDto());

    // WHEN
    var result = giftCertificateService.createGiftCertificate(new GiftCertificateDto());

    // THEN
    assertEquals(EntityModel.of(new GiftCertificateDto()), result);
  }

  @Test
  void
      getAllGiftCertificateDto_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // WHEN
    giftCertificateService.getAllGiftCertificateDto(0, 2);

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(
            giftCertificateRepository.findAll(PageRequest.of(0, 2)), giftCertificateDtoAssembler);
  }

  @Test
  void
      getGiftCertificateDtoById_shouldReturnGiftCertificateDtoEntityModel_whenGiftCertificateWithThisIdExist() {
    // GIVEN
    given(giftCertificateRepository.findById(anyLong()))
        .willReturn(Optional.of(new GiftCertificate()));
    given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class)))
        .willReturn(new GiftCertificateDto());

    // WHEN
    var result = giftCertificateService.getGiftCertificateDtoById(0L);

    // THEN
    assertEquals(EntityModel.of(new GiftCertificateDto()), result);
  }

  @Test
  void
      getGiftCertificateDtoById_shouldThrowGiftCertificateNotFoundException_whenGiftCertificateWithThisIdNotExist() {
    // GIVEN
    given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(
        GiftCertificateNotFoundException.class,
        () -> giftCertificateService.getGiftCertificateById(0L));
  }

  @Test
  void getGiftCertificateById_shouldReturnGiftCertificate_whenGiftCertificateWithThisIdExist() {
    // GIVEN
    given(giftCertificateRepository.findById(anyLong()))
        .willReturn(Optional.of(new GiftCertificate()));

    // WHEN
    var result = giftCertificateService.getGiftCertificateById(0L);

    // THEN
    assertEquals(new GiftCertificate(), result);
  }

  @Test
  void
      getGiftCertificateById_shouldThrowGiftCertificateNotFoundException_whenGiftCertificateWithThisIdNotExist() {
    // GIVEN
    given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(
        GiftCertificateNotFoundException.class,
        () -> giftCertificateService.getGiftCertificateById(0L));
  }

  @Test
  void
      getAllGiftCertificateDtoByParameters_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // GIVEN
    given(
            giftCertificateRepository.findByParameters(
                null, null, null, PageRequest.of(0, 2, Sort.unsorted())))
        .willReturn(Page.empty());

    // WHEN
    giftCertificateService.getAllGiftCertificateDtoByParameters(
        0, 2, new GiftCertificateSortingParametersDto());

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(Page.empty(), giftCertificateDtoAssembler);
  }

  @Test
  void
      updateGiftCertificate_shouldReturnGiftCertificateDtoEntityModel_whenGiftCertificateWithThisIdExist() {
    // GIVEN
    var giftCertificateForUpdate = new GiftCertificateDtoForUpdate();
    giftCertificateForUpdate.setId(0L);
    given(giftCertificateRepository.findById(anyLong()))
        .willReturn(Optional.of(new GiftCertificate()));
    given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class)))
        .willReturn(new GiftCertificateDto());

    // WHEN
    var result = giftCertificateService.updateGiftCertificate(giftCertificateForUpdate);

    // THEN
    assertEquals(EntityModel.of(new GiftCertificateDto()), result);
  }

  @Test
  void
      updateGiftCertificate_shouldThrowGiftCertificateNotFoundException_whenGiftCertificateWithThisIdNotExist() {
    // GIVEN
    var giftCertificateDtoForUpdate = new GiftCertificateDtoForUpdate();
    giftCertificateDtoForUpdate.setId(0L);
    given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(
        GiftCertificateNotFoundException.class,
        () -> giftCertificateService.updateGiftCertificate(giftCertificateDtoForUpdate));
  }

  @Test
  void deleteGiftCertificate_shouldCallsGiftCertificateRepositoryDeleteById_whenExecutedNormally() {

    // WHEN
    giftCertificateService.deleteGiftCertificate(0L);

    // THEN
    then(giftCertificateRepository).should(atLeastOnce()).deleteById(0L);
  }
}
