package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.aggregation.CountTagsId;
import com.epam.esm.giftcertificates.assembler.TagDtoAssembler;
import com.epam.esm.giftcertificates.repository.TagRepository;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.handler.exception.TagNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
  private final PagedResourcesAssembler<Tag> pagedResourcesAssembler =
      mock(PagedResourcesAssembler.class);
  private final TagService tagService =
      new TagServiceImpl(tagRepository, entityDtoMapper, tagDtoAssembler, pagedResourcesAssembler);

  @Test
  void createTag_shouldReturnTagDtoEntityModel_whenExecutedNormally() {
    // GIVEN
    given(entityDtoMapper.tagDtoToTag(any(TagDto.class))).willReturn(new Tag());
    given(tagDtoAssembler.toModel(any(Tag.class))).willReturn(new TagDto());

    // WHEN
    var result = tagService.createTag(new TagDto());

    // THEN
    assertEquals(EntityModel.of(new TagDto()), result);
  }

  @Test
  void getAllTags_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // WHEN
    tagService.getAllTags(0, 2);

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(tagRepository.findAll(PageRequest.of(0, 2)), tagDtoAssembler);
  }

  @Test
  void getTagById_shouldReturnTagDtoEntityModel_whenTagWithThisIdExist() {
    // GIVEN
    given(tagRepository.findById(anyLong())).willReturn(Optional.of(new Tag()));
    given(tagDtoAssembler.toModel(any(Tag.class))).willReturn(new TagDto());

    // WHEN
    var result = tagService.getTagById(0L);

    // THEN
    assertEquals(EntityModel.of(new TagDto()), result);
  }

  @Test
  void getTagById_shouldThrowTagNotFoundException_whenTagWithThisIdNotExist() {
    // GIVEN
    given(tagRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(TagNotFoundException.class, () -> tagService.getTagById(0L));
  }

  @Test
  void
      getMostWidelyUsedTagsFromPersonMaxCostRecipe_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // GIVEN
    var listCountTagsId = new ArrayList<CountTagsId>();
    var countTagsId = new CountTagsId() {
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
    given(tagRepository.findCountTagsInPersonMaxCostRecipe(anyLong())).willReturn(listCountTagsId);

    // WHEN
    tagService.getMostWidelyUsedTagsFromPersonMaxCostRecipe(0, 2, 0L);

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(
            tagRepository.findTagByIdIn(new ArrayList<>(), PageRequest.of(0, 2)), tagDtoAssembler);
  }

  @Test
  void getTagByName_shouldNotCallsTagRepositorySave_whenTagWithThisNameExist() {
    // GIVEN
    given(tagRepository.findTagByName(anyString())).willReturn(Optional.of(new Tag()));

    // WHEN
    tagService.getTagByName("tagName");

    // THEN
    then(tagRepository).should(times(0)).save(new Tag());
  }

  @Test
  void getTagByName_shouldCallsTagRepositorySave_whenTagWithThisNameNotExist() {
    // GIVEN
    given(tagRepository.findTagByName(anyString())).willReturn(Optional.empty());

    // WHEN
    tagService.getTagByName("tagName");

    // THEN
    then(tagRepository).should(atLeastOnce()).save(new Tag());
  }

  @Test
  void getTagsByGiftCertificateId_shouldReturnSetOfTags_whenExecutedNormally() {
    // GIVEN
    given(tagRepository.findTagsByGiftCertificatesId(anyLong())).willReturn(Collections.emptySet());

    // WHEN
    var result = tagService.getTagsByGiftCertificateId(0L);

    // THEN
    assertEquals(Collections.emptySet(), result);
  }

  @Test
  void deleteTag_shouldCallsTagRepositoryDeleteById_whenExecutedNormally() {
    // WHEN
    tagService.deleteTag(0L);

    // THEN
    then(tagRepository).should(atLeastOnce()).deleteById(0L);
  }
}
