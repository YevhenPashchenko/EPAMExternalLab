package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDtoForUpdate;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.giftcertificates.util.TestEntityFactory;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class GiftCertificateServiceImplTest {

    private final GiftCertificateRepository giftCertificateRepository = mock(GiftCertificateRepository.class);
    private final TagService tagService = mock(TagService.class);
    private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
    private final GiftCertificateDtoAssembler giftCertificateDtoAssembler = mock(GiftCertificateDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler =
        mock(PagedResourcesAssembler.class);
    private final GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl(
        giftCertificateRepository, tagService, entityDtoMapper, giftCertificateDtoAssembler, pagedResourcesAssembler);

    @Test
    void createGiftCertificate_shouldReturnGiftCertificate_whenExecutedNormally() {
        // GIVEN
        var giftCertificateDto = TestEntityFactory.createDefaultGiftCertificateDto();
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(any(GiftCertificateDto.class))).willReturn(
            giftCertificate);
        given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class))).willReturn(giftCertificateDto);

        // WHEN
        var result = giftCertificateService.createGiftCertificate(giftCertificateDto);

        // THEN
        assertThat(EntityModel.of(giftCertificateDto)).isEqualTo(result);
    }

    @Test
    void getAllGiftCertificates_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        giftCertificateService.getAllGiftCertificates(0, 2);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(giftCertificateRepository.findAll(PageRequest.of(0, 2)), giftCertificateDtoAssembler);
    }

    @Test
    void
    getGiftCertificateDtoById_shouldReturnGiftCertificate_whenGiftCertificateWithThisIdExist() {
        // GIVEN
        var giftCertificateDto = TestEntityFactory.createDefaultGiftCertificateDto();
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.of(giftCertificate));
        given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class))).willReturn(giftCertificateDto);

        // WHEN
        var result = giftCertificateService.getGiftCertificateDtoById(0L);

        // THEN
        assertThat(EntityModel.of(giftCertificateDto)).isEqualTo(result);
    }

    @Test
    void
    getGiftCertificateDtoById_shouldThrowEntityNotFoundException_whenGiftCertificateWithThisIdNotExist() {
        // GIVEN
        given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.getGiftCertificateById(0L));
    }

    @Test
    void getGiftCertificateById_shouldReturnGiftCertificate_whenGiftCertificateWithThisIdExist() {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.of(giftCertificate));

        // WHEN
        var result = giftCertificateService.getGiftCertificateById(0L);

        // THEN
        assertThat(giftCertificate).isEqualTo(result);
    }

    @Test
    void
    getGiftCertificateById_shouldThrowEntityNotFoundException_whenGiftCertificateWithThisIdNotExist() {
        // GIVEN
        given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.getGiftCertificateById(0L));
    }

    @Test
    void
    getAllGiftCertificatesByParameters_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // GIVEN
        given(giftCertificateRepository.findByParameters(null, null, null,
            PageRequest.of(0, 2))).willReturn(Page.empty());

        // WHEN
        giftCertificateService.getAllGiftCertificatesByParameters(0, 2, new GiftCertificateSortingParametersDto());

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce()).toModel(Page.empty(), giftCertificateDtoAssembler);
    }

    @Test
    void
    getAllGiftCertificatesByTags_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificateDto();
        given(giftCertificateRepository.findGiftCertificatesByTagsId(new HashSet<>(), 0,
            PageRequest.of(0, 2))).willReturn(Page.empty());

        // WHEN
        giftCertificateService.getAllGiftCertificatesByTags(0, 2, giftCertificate);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce()).toModel(Page.empty(), giftCertificateDtoAssembler);
    }

    @Test
    void
    updateGiftCertificate_shouldReturnGiftCertificate_whenGiftCertificateWithThisIdExist() {
        // GIVEN
        var giftCertificateDto = TestEntityFactory.createDefaultGiftCertificateDto();
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.of(giftCertificate));
        given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class))).willReturn(giftCertificateDto);

        // WHEN
        var result = giftCertificateService.updateGiftCertificate(0L, new GiftCertificateDtoForUpdate());

        // THEN
        assertThat(EntityModel.of(giftCertificateDto)).isEqualTo(result);
    }

    @Test
    void
    updateGiftCertificate_shouldThrowEntityNotFoundException_whenGiftCertificateWithThisIdNotExist() {
        // GIVEN
        given(giftCertificateRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> giftCertificateService.updateGiftCertificate(0L, new GiftCertificateDtoForUpdate()));
    }

    @Test
    void deleteGiftCertificate_shouldCallsGiftCertificateRepositoryDeleteById_whenExecutedNormally() {

        // WHEN
        giftCertificateService.deleteGiftCertificate(0L);

        // THEN
        then(giftCertificateRepository).should(atLeastOnce()).deleteById(0L);
    }
}
