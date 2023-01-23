package com.epam.esm.giftcertificates.unit.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateDao;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificateSortingParameters;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.handler.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.GiftCertificateToTagService;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class GiftCertificateServiceImplTest {

    private final GiftCertificateDao giftCertificateDao = mock(GiftCertificateDao.class);
    private final TagService tagService = mock(TagService.class);
    private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
    private final GiftCertificateToTagService giftCertificateToTagService = mock(GiftCertificateToTagService.class);
    private final GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao,
            tagService, giftCertificateToTagService, entityDtoMapper);

    @Test
    void createGiftCertificate_shouldCallsTagServiceCreateTag_whenGiftCertificateDtoHasNewTagName() {
        //GIVEN
        var giftCertificateDTO = new GiftCertificateDto();
        var tagDTO = new TagDto();
        tagDTO.setName("tagName");
        giftCertificateDTO.setTags(List.of(tagDTO));
        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDTO)).willReturn(new GiftCertificate());

        //WHEN
        giftCertificateService.createGiftCertificate(giftCertificateDTO);

        //THEN
        then(tagService).should(times(1)).createTag(tagDTO);
    }

    @Test
    void createGiftCertificate_shouldCallsGiftCertificateToTagService_whenGiftCertificateDtoHasTags() {
        //GIVEN
        var giftCertificateDTO = new GiftCertificateDto();
        var tagDTO = new TagDto();
        tagDTO.setName("tagName");
        giftCertificateDTO.setTags(List.of(tagDTO));
        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDTO)).willReturn(new GiftCertificate());

        //WHEN
        giftCertificateService.createGiftCertificate(giftCertificateDTO);

        //THEN
        then(giftCertificateToTagService).should(times(1))
                .createGiftCertificateToTagLink(any(GiftCertificateToTagDto.class));
    }

    @Test
    void getListOfGiftCertificateDtoForPage_shouldReturnListOfGiftCertificateDtoForPage_whenExecutedNormally() {
        //GIVEN
        given(giftCertificateDao.getListOfGiftCertificateForPage(anyInt())).willReturn(Collections.emptyList());

        //WHEN
        var result = giftCertificateService.getListOfGiftCertificateDtoForPage(1);

        //THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getGiftCertificateDtoById_shouldReturnGiftCertificateDto_whenGiftCertificateWithThisIdExist() {
        // GIVEN
        var giftCertificateDto = new GiftCertificateDto();
        given(entityDtoMapper.giftCertificateToGiftCertificateDto(any(GiftCertificate.class))).willReturn(new GiftCertificateDto());
        given(giftCertificateDao.getGiftCertificateById(giftCertificateDto.getId())).willReturn(Optional.of(new GiftCertificate()));

        // WHEN
        var result = giftCertificateService.getGiftCertificateDtoById(giftCertificateDto.getId());

        // THEN
        assertEquals(giftCertificateDto, result);
    }

    @Test
    void getGiftCertificateDtoById_shouldThrowGiftCertificateNotFoundException_whenGiftCertificateWithThisIdNotExist() {
        // GIVEN
        given(giftCertificateDao.getGiftCertificateById(anyInt())).willReturn(Optional.empty());

        // THEN
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.getGiftCertificateDtoById(1));
    }

    @Test
    void getListOfGiftCertificateDtoByParametersForPage_shouldReturnListOfGiftCertificateDtoByParametersForPage_whenExecutedNormally() {
        // GIVEN
        var pageNumber = 1;
        given(entityDtoMapper
                .giftCertificateSortingParametersDtoToGiftCertificateSortingParameters(any(GiftCertificateSortingParametersDto.class)))
                .willReturn(new GiftCertificateSortingParameters());

        // WHEN
        var result = giftCertificateService.getListOfGiftCertificateDtoByParametersForPage(pageNumber,
                new GiftCertificateSortingParametersDto());

        // THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void updateGiftCertificate_shouldCallsEachOfGiftCertificateDaoUpdateMethods_whenExecutesWithAllGiftCertificateDtoFieldsNotEmpty() {
        // GIVEN
        var name = "name";
        var description = "description";
        var price = BigDecimal.TEN;
        var duration = 30;

        var giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);

        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(any(GiftCertificateDto.class))).willReturn(giftCertificate);

        // WHEN
        giftCertificateService.updateGiftCertificate(new GiftCertificateDto());

        // THEN
        then(giftCertificateDao).should(times(1)).updateGiftCertificateName(giftCertificate);
        then(giftCertificateDao).should(times(1)).updateGiftCertificateDescription(giftCertificate);
        then(giftCertificateDao).should(times(1)).updateGiftCertificatePrice(giftCertificate);
        then(giftCertificateDao).should(times(1)).updateGiftCertificateDuration(giftCertificate);
    }

    @Test
    void deleteGiftCertificate_shouldCallsGiftCertificateDaoDeleteGiftCertificate_whenExecutedNormally() {
        //GIVEN
        var id = 1;

        //WHEN
        giftCertificateService.deleteGiftCertificate(id);

        //THEN
        then(giftCertificateDao).should(times(1)).deleteGiftCertificate(id);
    }
}
