package com.epam.esm.giftcertificate.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateDao;
import com.epam.esm.giftcertificates.entities.GiftCertificateSortingParameters;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.handlers.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.mappers.EntityDtoMapper;
import com.epam.esm.giftcertificates.services.GiftCertificateService;
import com.epam.esm.giftcertificates.services.GiftCertificateToTagService;
import com.epam.esm.giftcertificates.services.TagService;
import com.epam.esm.giftcertificates.services.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class GiftCertificateServiceImplTest {

    private final GiftCertificateDao giftCertificateDao = Mockito.mock(GiftCertificateDao.class);
    private final TagService tagService = Mockito.mock(TagService.class);
    private final EntityDtoMapper entityDtoMapper = Mockito.mock(EntityDtoMapper.class);
    private final GiftCertificateToTagService giftCertificateToTagService = Mockito.mock(GiftCertificateToTagService.class);
    private final GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao,
            tagService, giftCertificateToTagService, entityDtoMapper);

    @Test
    void create_whenGiftCertificateDtoHasNewTags_tagServiceCreateCalls() {
        //GIVEN
        var giftCertificateDTO = new GiftCertificateDto();
        var tagDTO = new TagDto();
        tagDTO.setName("tagName");
        giftCertificateDTO.setTags(List.of(tagDTO));
        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDTO)).willReturn(new GiftCertificate());

        //WHEN
        giftCertificateService.create(giftCertificateDTO);

        //THEN
        then(tagService).should(times(1)).create(tagDTO);
    }

    @Test
    void create_whenGiftCertificateDtoHasTags_giftCertificateToTagServiceCreateCalls() {
        //GIVEN
        var giftCertificateDTO = new GiftCertificateDto();
        var tagDTO = new TagDto();
        tagDTO.setName("tagName");
        giftCertificateDTO.setTags(List.of(tagDTO));
        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDTO)).willReturn(new GiftCertificate());

        //WHEN
        giftCertificateService.create(giftCertificateDTO);

        //THEN
        then(giftCertificateToTagService).should(times(1)).create(new GiftCertificateToTagDto());
    }

    @Test
    void get_whenExecute_shouldReturnListOfGiftCertificateDTOs() {
        //GIVEN
        var pageNumber = 1;
        given(giftCertificateDao.get(pageNumber)).willReturn(Collections.emptyList());

        //WHEN
        var result = giftCertificateService.get(pageNumber);

        //THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getById_whenGiftCertificateExist_shouldReturnIt() {
        // GIVEN
        var giftCertificateDTO = new GiftCertificateDto();
        given(entityDtoMapper.giftCertificateToGiftCertificateDto(new GiftCertificate())).willReturn(new GiftCertificateDto());
        given(giftCertificateDao.getById(giftCertificateDTO.getId())).willReturn(Optional.of(new GiftCertificate()));

        // WHEN
        var result = giftCertificateService.getById(giftCertificateDTO.getId());

        // THEN
        assertEquals(giftCertificateDTO, result);
    }

    @Test
    void getById_whenGiftCertificateNotExist_shouldThrowGiftCertificateNotFoundException() {
        // GIVEN
        var id = 0;
        given(giftCertificateDao.getById(id)).willReturn(Optional.empty());

        // THEN
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.getById(id));
    }

    @Test
    void getByParameters_whenExecute_shouldReturnListOfGiftCertificateDTOs() {
        // GIVEN
        var pageNumber = 1;
        given(entityDtoMapper
                .giftCertificateSortingParametersDtoToGiftCertificateSortingParameters(new GiftCertificateSortingParametersDto()))
                .willReturn(new GiftCertificateSortingParameters());

        // WHEN
        var result = giftCertificateService.getByParameters(pageNumber,
                new GiftCertificateSortingParametersDto());

        // THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void update_whenExecuteWithAllGiftCertificateDtoFieldsNotEmpty_shouldCallEachGiftCertificateDaoUpdateMethod() {
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

        given(entityDtoMapper.giftCertificateDtoToGiftCertificate(new GiftCertificateDto())).willReturn(giftCertificate);

        // WHEN
        giftCertificateService.update(new GiftCertificateDto());

        // THEN
        then(giftCertificateDao).should(times(1)).updateName(giftCertificate);
        then(giftCertificateDao).should(times(1)).updateDescription(giftCertificate);
        then(giftCertificateDao).should(times(1)).updatePrice(giftCertificate);
        then(giftCertificateDao).should(times(1)).updateDuration(giftCertificate);
    }

    @Test
    void delete_whenExecute_giftCertificateDaoDeleteCalls() {
        //GIVEN
        var id = 0;

        //WHEN
        giftCertificateService.delete(id);

        //THEN
        then(giftCertificateDao).should(times(1)).delete(id);
    }
}
