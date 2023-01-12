package com.epam.esm.giftcertificate.services.impl;

import com.epam.esm.giftcertificates.dao.TagDao;
import com.epam.esm.giftcertificates.entities.Tag;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.handlers.exceptions.TagNotFoundException;
import com.epam.esm.giftcertificates.mappers.EntityDtoMapper;
import com.epam.esm.giftcertificates.services.TagService;
import com.epam.esm.giftcertificates.services.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class TagServiceImplTest {

    private final TagDao tagDao = Mockito.mock(TagDao.class);
    private final EntityDtoMapper entityDtoMapper = Mockito.mock(EntityDtoMapper.class);
    private final TagService tagService = new TagServiceImpl(tagDao, entityDtoMapper);

    @Test
    void create_whenExecute_shouldReturnCreatedTagId() {
        //GIVEN
        var id = 0;

        //WHEN
        var result = tagService.create(new TagDto());

        //THEN
        assertEquals(id, result);
    }

    @Test
    void get_whenExecute_shouldReturnListOfTagDTOs() {
        //GIVEN
        given(tagDao.get()).willReturn(Collections.emptyList());

        //WHEN
        var result = tagService.get();

        //THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getById_whenTagExist_shouldReturnIt() {
        //GIVEN
        var tagDTO = new TagDto();
        given(tagDao.getById(tagDTO.getId())).willReturn(Optional.of(new Tag()));
        given(entityDtoMapper.tagToTagDto(new Tag())).willReturn(tagDTO);

        //WHEN
        var result = tagService.getById(tagDTO.getId());

        //THEN
        assertEquals(tagDTO, result);
    }

    @Test
    void getById_whenTagNotExist_shouldThrowTagNotFoundException() {
        //GIVEN
        var id = 0;
        given(tagDao.getById(id)).willReturn(Optional.empty());

        //THEN
        assertThrows(TagNotFoundException.class, () -> tagService.getById(id));
    }

    @Test
    void getByGiftCertificateId_whenExecute_shouldReturnListOfTagDTOs() {
        //GIVEN
        var id = 0;

        //WHEN
        var result = tagService.getByGiftCertificateId(id);

        //THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void delete_whenExecute_tagDaoDeleteCalls() {
        //GIVEN
        var id = 0;

        //WHEN
        tagService.delete(id);

        //THEN
        then(tagDao).should(times(1)).delete(id);
    }
}
