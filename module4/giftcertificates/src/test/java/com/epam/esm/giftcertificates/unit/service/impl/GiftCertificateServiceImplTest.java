package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.dto.TagNamesDto;
import com.epam.esm.giftcertificates.dto.UpdateGiftCertificateDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class GiftCertificateServiceImplTest {

    private static final String TAG1_NAME = "tagName1";
    private static final String TAG2_NAME = "tagName2";
    private static final int PAGE = 0;
    private static final int SIZE = 2;
    private final GiftCertificateRepository giftCertificateRepository = mock(GiftCertificateRepository.class);
    private final TagService tagService = mock(TagService.class);
    private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
    private final GiftCertificateDtoAssembler giftCertificateDtoAssembler = mock(GiftCertificateDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler =
        mock(PagedResourcesAssembler.class);
    private final GiftCertificateService giftCertificateService =
        new GiftCertificateServiceImpl(giftCertificateRepository, tagService, entityDtoMapper,
            giftCertificateDtoAssembler, pagedResourcesAssembler);

    @Test
    void createGiftCertificate_shouldReturnGiftCertificate_whenExecutedNormally() {
        // GIVEN
        var giftCertificateDto = TestEntityFactory.createDefaultGiftCertificateDto();
        giftCertificateDto.setTags(new HashSet<>(Set.of(TestEntityFactory.createDefaultTagDto(TAG1_NAME),
            TestEntityFactory.createDefaultTagDto(TAG2_NAME))));
        given(tagService.isExist(TAG1_NAME)).willReturn(true);
        given(tagService.getTagByName(TAG1_NAME)).willReturn(TestEntityFactory.createDefaultTag(TAG1_NAME));
        given(entityDtoMapper.tagToTagDto(any(Tag.class))).willReturn(TestEntityFactory.createDefaultTagDto(TAG1_NAME));
        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDto)).willReturn(
            TestEntityFactory.createDefaultGiftCertificate());
        given(entityDtoMapper.tagDtoToTag(any(TagDto.class))).willReturn(TestEntityFactory.createDefaultTag(TAG2_NAME));
        given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class))).willReturn(giftCertificateDto);

        // WHEN
        var result = giftCertificateService.createGiftCertificate(giftCertificateDto);

        // THEN
        assertThat(EntityModel.of(giftCertificateDto)).isEqualTo(result);
    }

    @Test
    void getAllGiftCertificates_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        giftCertificateService.getAllGiftCertificates(PAGE, SIZE);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(giftCertificateRepository.findAll(PageRequest.of(PAGE, SIZE)), giftCertificateDtoAssembler);
    }

    @Test
    void getGiftCertificateByName_shouldReturnGiftCertificate_whenGiftCertificateWithThisNameExist() {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        given(giftCertificateRepository.findByName(anyString())).willReturn(Optional.of(giftCertificate));

        // WHEN
        var result = giftCertificateService.getGiftCertificateByName(giftCertificate.getName());

        // THEN
        assertThat(giftCertificate).isEqualTo(result);
    }

    @Test
    void getGiftCertificateByName_shouldThrowEntityNotFoundException_whenGiftCertificateWithThisNameNotExist() {
        // GIVEN
        given(giftCertificateRepository.findByName(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.getGiftCertificateByName(TAG1_NAME));
    }

    @Test
    void getAllGiftCertificatesByParameters_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // GIVEN
        var parameters = TestEntityFactory.createDefaultGiftCertificateSortingParametersDto();
        given(giftCertificateRepository.findByParameters(parameters.getTagName(), parameters.getPartName(),
            parameters.getPartDescription(), PageRequest.of(PAGE, SIZE, Sort.by(Direction.DESC, "name")))).willReturn(
            Page.empty());

        // WHEN
        giftCertificateService.getAllGiftCertificatesByParameters(PAGE, SIZE, parameters);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce()).toModel(Page.empty(), giftCertificateDtoAssembler);
    }

    @Test
    void getAllGiftCertificatesByTags_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // GIVEN
        given(giftCertificateRepository.findGiftCertificatesByTagNames(eq(Collections.emptySet()), anyInt(),
            eq(PageRequest.of(PAGE, SIZE)))).willReturn(Page.empty());

        // WHEN
        giftCertificateService.getAllGiftCertificatesByTags(PAGE, SIZE, new TagNamesDto());

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce()).toModel(Page.empty(), giftCertificateDtoAssembler);
    }

    @Test
    void updateGiftCertificate_shouldReturnGiftCertificate_whenGiftCertificateWithThisNameExist() {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG1_NAME);
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        var updateGiftCertificate = new UpdateGiftCertificateDto();
        updateGiftCertificate.setTags(Set.of(TestEntityFactory.createDefaultTagDto(TAG1_NAME)));
        var giftCertificateDto = TestEntityFactory.createDefaultGiftCertificateDto();
        given(giftCertificateRepository.findByName(anyString())).willReturn(Optional.of(giftCertificate));
        given(tagService.isExist(TAG1_NAME)).willReturn(true);
        given(tagService.getTagByName(TAG1_NAME)).willReturn(tag);
        given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class))).willReturn(giftCertificateDto);

        // WHEN
        var result = giftCertificateService.updateGiftCertificate(giftCertificate.getName(), updateGiftCertificate);

        // THEN
        assertThat(EntityModel.of(giftCertificateDto)).isEqualTo(result);
    }

    @Test
    void updateGiftCertificate_shouldThrowEntityNotFoundException_whenGiftCertificateWithThisNameNotExist() {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        given(giftCertificateRepository.findByName(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> giftCertificateService.updateGiftCertificate(giftCertificate.getName(),
                new UpdateGiftCertificateDto()));
    }

    @Test
    void deleteGiftCertificate_shouldReturnGiftCertificate_whenGiftCertificateWithThisNameExist() {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        var giftCertificateDto = TestEntityFactory.createDefaultGiftCertificateDto();
        given(giftCertificateRepository.deleteByName(anyString())).willReturn(List.of(giftCertificate));
        given(giftCertificateDtoAssembler.toModel(any(GiftCertificate.class))).willReturn(giftCertificateDto);

        // WHEN
        var result = giftCertificateService.deleteGiftCertificate(giftCertificate.getName());

        // THEN
        assertThat(EntityModel.of(giftCertificateDto)).isEqualTo(result);
    }

    @Test
    void deleteGiftCertificate_shouldThrowEntityNotFoundException_whenGiftCertificateWithThisNameNotExist() {
        // GIVEN
        var giftCertificate = TestEntityFactory.createDefaultGiftCertificate();
        given(giftCertificateRepository.deleteByName(anyString())).willReturn(Collections.emptyList());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.deleteGiftCertificate(giftCertificate.getName()));
    }
}
