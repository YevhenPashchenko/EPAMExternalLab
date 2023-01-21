package unit.com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.TagDao;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.handler.exceptions.TagNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class TagServiceImplTest {

    private final TagDao tagDao = Mockito.mock(TagDao.class);
    private final EntityDtoMapper entityDtoMapper = Mockito.mock(EntityDtoMapper.class);
    private final TagService tagService = new TagServiceImpl(tagDao, entityDtoMapper);

    @Test
    void createTag_shouldReturnCreatedTagId_whenExecuteNormally() {
        //GIVEN
        var id = 0;

        //WHEN
        var result = tagService.createTag(new TagDto());

        //THEN
        assertEquals(id, result);
    }

    @Test
    void getListOfTagDto_shouldReturnListOfTagDto_whenExecuteNormally() {
        //GIVEN
        given(tagDao.getListOfTag()).willReturn(Collections.emptyList());

        //WHEN
        var result = tagService.getListOfTagDto();

        //THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getTagDtoById_shouldReturnTagDto_whenTagWithThisIdExist() {
        //GIVEN
        var tagDTO = new TagDto();
        given(tagDao.getTagById(tagDTO.getId())).willReturn(Optional.of(new Tag()));
        given(entityDtoMapper.tagToTagDto(any(Tag.class))).willReturn(tagDTO);

        //WHEN
        var result = tagService.getTagDtoById(tagDTO.getId());

        //THEN
        assertEquals(tagDTO, result);
    }

    @Test
    void getTagDtoById_shouldThrowTagNotFoundException_whenTagWithThisIdNotExist() {
        //GIVEN
        given(tagDao.getTagById(anyInt())).willReturn(Optional.empty());

        //THEN
        assertThrows(TagNotFoundException.class, () -> tagService.getTagDtoById(1));
    }

    @Test
    void getListOfTagDtoByGiftCertificateId_shouldReturnListOfTagDto_whenExecuteNormally() {
        //GIVEN
        var id = 0;

        //WHEN
        var result = tagService.getListOfTagDtoByGiftCertificateId(id);

        //THEN
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void deleteTag_shouldCallsTagDaoDeleteTag_whenExecuteNormally() {
        //GIVEN
        var id = 0;

        //WHEN
        tagService.deleteTag(id);

        //THEN
        then(tagDao).should(times(1)).deleteTag(id);
    }
}
