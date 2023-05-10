package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.TagDtoAssembler;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.entity.projection.TagsCountToTagsIdProjection;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.repository.TagRepository;
import com.epam.esm.giftcertificates.service.TagService;
import com.epam.esm.giftcertificates.service.impl.TagServiceImpl;
import com.epam.esm.giftcertificates.util.TestEntityFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class TagServiceImplTest {

    private static final String TAG_NAME = "tagName1";
    private static final String EMAIL = "email@mail.com";
    private static final int PAGE = 0;
    private static final int SIZE = 2;
    private final TagRepository tagRepository = mock(TagRepository.class);
    private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
    private final TagDtoAssembler tagDtoAssembler = mock(TagDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler = mock(PagedResourcesAssembler.class);
    private final TagService tagService =
        new TagServiceImpl(tagRepository, entityDtoMapper, tagDtoAssembler, pagedResourcesAssembler);

    @BeforeEach
    public void setContext() {
        var authentication = new UsernamePasswordAuthenticationToken((Principal) () -> EMAIL, "password");
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createTag_shouldReturnTag_whenExecutedNormally() {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG_NAME);
        var tagDto = TestEntityFactory.createDefaultTagDto(TAG_NAME);
        given(entityDtoMapper.tagDtoToTag(tagDto)).willReturn(tag);
        given(tagRepository.save(any(Tag.class))).willReturn(tag);
        given(tagDtoAssembler.toModel(any(Tag.class))).willReturn(tagDto);

        // WHEN
        var result = tagService.createTag(tagDto);

        // THEN
        assertThat(EntityModel.of(tagDto)).isEqualTo(result);
    }

    @Test
    void getAllTags_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        tagService.getAllTags(PAGE, SIZE);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(tagRepository.findAll(PageRequest.of(PAGE, SIZE)), tagDtoAssembler);
    }

    @Test
    void getTagByName_shouldReturnTag_whenTagWithThisNameExist() {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTag(TAG_NAME);
        given(tagRepository.findTagByName(anyString())).willReturn(Optional.of(tag));

        // WHEN
        var result = tagService.getTagByName(TAG_NAME);

        // THEN
        assertThat(tag).isEqualTo(result);
    }

    @Test
    void getTagByName_shouldThrowEntityNotFoundException_whenTagWithThisNameNotExist() {
        // GIVEN
        given(tagRepository.findTagByName(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> tagService.getTagByName(TAG_NAME));
    }

    @Test
    void getMostWidelyUsedTagsFromPersonMaxCostRecipe_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // GIVEN
        given(tagRepository.findCountTagsInPersonMaxCostReceipt(anyString())).willReturn(List.of(
            new TagsCountToTagsIdProjection() {
                @Override
                public Long getTagCount() {
                    return 1L;
                }

                @Override
                public Long getTagId() {
                    return 1L;
                }
            }));

        // WHEN
        tagService.getMostWidelyUsedTagsFromPersonMaxCostReceipt(PAGE, SIZE);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(tagRepository.findTagByIdIn(Collections.emptyList(), PageRequest.of(PAGE, SIZE)), tagDtoAssembler);
    }

    @Test
    void getMostWidelyUsedTagsFromPersonMaxCostRecipe_shouldThrowEntityNotFoundException_whenAnyTagNotFound() {
        // GIVEN
        given(tagRepository.findCountTagsInPersonMaxCostReceipt(anyString())).willReturn(Collections.emptyList());

        // THEN
        assertThrows(EntityNotFoundException.class,
            () -> tagService.getMostWidelyUsedTagsFromPersonMaxCostReceipt(PAGE, SIZE));
    }

    @Test
    void deleteTag_shouldReturnTag_whenTagWithThisNameExist() {
        // GIVEN
        var tag = TestEntityFactory.createDefaultTagDto(TAG_NAME);
        given(tagRepository.deleteByName(TAG_NAME)).willReturn(List.of(TestEntityFactory.createDefaultTag(TAG_NAME)));
        given(tagDtoAssembler.toModel(any(Tag.class))).willReturn(tag);

        // WHEN
        var result = tagService.deleteTag(TAG_NAME);

        // THEN
        assertThat(EntityModel.of(tag)).isEqualTo(result);
    }

    @Test
    void deleteTag_shouldThrowEntityNotFoundException_whenTagWithThisNameNotExist() {
        // GIVEN
        given(tagRepository.deleteByName(TAG_NAME)).willReturn(Collections.emptyList());

        // THEN
        assertThrows(EntityNotFoundException.class, () -> tagService.deleteTag(TAG_NAME));
    }

    @Test
    void isExist_shouldReturnTrue_whenTagWithThisNameExist() {
        given(tagRepository.existsByName(anyString())).willReturn(true);

        // WHEN
        var result = tagService.isExist(TAG_NAME);

        // THEN
        assertTrue(result);
    }

    @Test
    void isExist_shouldReturnFalse_whenTagWithThisNameNotExist() {
        given(tagRepository.existsByName(anyString())).willReturn(false);

        // WHEN
        var result = tagService.isExist(TAG_NAME);

        // THEN
        assertFalse(result);
    }
}
