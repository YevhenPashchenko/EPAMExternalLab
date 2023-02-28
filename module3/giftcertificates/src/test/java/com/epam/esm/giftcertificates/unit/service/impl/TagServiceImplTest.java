package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.entity.projection.TagsCountToTagsIdProjection;
import com.epam.esm.giftcertificates.assembler.TagDtoAssembler;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.repository.TagRepository;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.TagServiceImpl;

import java.util.Set;

import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class TagServiceImplTest {

    private final TagRepository tagRepository = mock(TagRepository.class);
    private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
    private final TagDtoAssembler tagDtoAssembler = mock(TagDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler = mock(PagedResourcesAssembler.class);
    private final TagService tagService =
        new TagServiceImpl(tagRepository, entityDtoMapper, tagDtoAssembler, pagedResourcesAssembler);

    @Test
    void createTag_shouldReturnTag_whenExecutedNormally() {
        // GIVEN
        var tagDto = TestEntityFactory.createDefaultTagDto();
        var tag = TestEntityFactory.createDefaultTag();
        given(entityDtoMapper.tagDtoToTag(any(TagDto.class))).willReturn(tag);
        given(tagDtoAssembler.toModel(any(Tag.class))).willReturn(tagDto);

        // WHEN
        var result = tagService.createTag(new TagDto());

        // THEN
        assertThat(EntityModel.of(tagDto)).isEqualTo(result);
    }

    @Test
    void getAllTags_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        tagService.getAllTags(0, 2);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(tagRepository.findAll(PageRequest.of(0, 2)), tagDtoAssembler);
    }

    @Test
    void getTagById_shouldReturnTag_whenTagWithThisIdExist() {
        // GIVEN
        var tagDto = TestEntityFactory.createDefaultTagDto();
        var tag = TestEntityFactory.createDefaultTag();
        given(tagRepository.findById(anyLong())).willReturn(Optional.of(tag));
        given(tagDtoAssembler.toModel(any(Tag.class))).willReturn(tagDto);

        // WHEN
        var result = tagService.getTagById(0L);

        // THEN
        assertThat(EntityModel.of(tagDto)).isEqualTo(result);
    }

    @Test
    void getTagById_shouldThrowEntityNotFoundException_whenTagWithThisIdNotExist() {
        // GIVEN
        given(tagRepository.findById(anyLong())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> tagService.getTagById(0L));
    }

    @Test
    void getMostWidelyUsedTagsFromPersonMaxCostRecipe_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // GIVEN
        var listCountTagsId = new ArrayList<TagsCountToTagsIdProjection>();
        var countTagsId = new TagsCountToTagsIdProjection() {
            @Override
            public Long getTagCount() {
                return 1L;
            }

            @Override
            public Long getTagId() {
                return 1L;
            }
        };
        listCountTagsId.add(countTagsId);
        given(tagRepository.findCountTagsInPersonMaxCostReceipt(anyLong())).willReturn(listCountTagsId);

        // WHEN
        tagService.getMostWidelyUsedTagsFromPersonMaxCostReceipt(0, 2, 0L);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(tagRepository.findTagByIdIn(new ArrayList<>(), PageRequest.of(0, 2)), tagDtoAssembler);
    }

    @Test
    void getTagByName_shouldNotCallsTagRepositorySave_whenTagWithThisNameExist() {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag();
        given(tagRepository.findTagByName(anyString())).willReturn(Optional.of(tag));

        // WHEN
        tagService.getTagByName(tag.getName());

        // THEN
        then(tagRepository).should(times(0)).save(tag);
    }

    @Test
    void getTagByName_shouldCallsTagRepositorySave_whenTagWithThisNameNotExist() {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag();
        tag.setId(null);
        given(tagRepository.findTagByName(anyString())).willReturn(Optional.empty());

        // WHEN
        tagService.getTagByName(tag.getName());

        // THEN
        then(tagRepository).should(atLeastOnce()).save(tag);
    }

    @Test
    void getTagsByGiftCertificateId_shouldReturnSetOfTags_whenExecutedNormally() {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag();
        given(tagRepository.findTagsByGiftCertificatesId(anyLong())).willReturn(Set.of(tag));

        // WHEN
        var result = tagService.getTagsByGiftCertificateId(0L);

        // THEN
        assertThat(Set.of(tag)).isEqualTo(result);
    }

    @Test
    void deleteTag_shouldCallsTagRepositoryDeleteById_whenExecutedNormally() {
        // WHEN
        tagService.deleteTag(0L);

        // THEN
        then(tagRepository).should(atLeastOnce()).deleteById(0L);
    }
}
