package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.TagDtoAssembler;
import com.epam.esm.giftcertificates.constant.HttpErrorCodes;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.entity.projection.TagsCountToTagsIdProjection;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.repository.TagRepository;
import com.epam.esm.giftcertificates.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final TagDtoAssembler tagDtoAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();

    @Override
    public EntityModel<TagDto> createTag(TagDto tag) {
        return EntityModel.of(tagDtoAssembler.toModel(tagRepository.save(entityDtoMapper.tagDtoToTag(tag))));
    }

    @Override
    public PagedModel<TagDto> getAllTags(int page, int size) {
        return pagedResourcesAssembler.toModel(tagRepository.findAll(PageRequest.of(page, size)), tagDtoAssembler);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findTagByName(name)
            .orElseThrow(() -> new EntityNotFoundException(getErrorMessage(name),
                HttpErrorCodes.TAG_NOT_FOUND));
    }

    private String getErrorMessage(String name) {
        return "Requested tag not found (name = " + name + ")";
    }

    @Override
    public PagedModel<TagDto> getMostWidelyUsedTagsFromPersonMaxCostReceipt(int page, int size) {
        var countTagsIdFromPersonMaxCostReceipt = tagRepository.findCountTagsInPersonMaxCostReceipt(
            securityContextHolderStrategy.getContext().getAuthentication().getName());
        var maxCount = getMaxCountTagIdInPersonMaxCostReceipt(countTagsIdFromPersonMaxCostReceipt);
        return pagedResourcesAssembler.toModel(
            tagRepository.findTagByIdIn(getTagsIdCompareToMaxCount(countTagsIdFromPersonMaxCostReceipt, maxCount),
                PageRequest.of(page, size)), tagDtoAssembler);
    }

    private Long getMaxCountTagIdInPersonMaxCostReceipt(List<TagsCountToTagsIdProjection> countTagsIdList) {
        return countTagsIdList.stream().mapToLong(TagsCountToTagsIdProjection::getTagCount).max().orElseThrow(
            () -> new EntityNotFoundException("Most widely used tag in recipe with max cost not found",
                HttpErrorCodes.TAG_NOT_FOUND));
    }

    private List<Long> getTagsIdCompareToMaxCount(List<TagsCountToTagsIdProjection> countTagsIdList, Long maxCount) {
        return countTagsIdList.stream().filter(countTagsId -> Objects.equals(countTagsId.getTagCount(), maxCount))
            .map(TagsCountToTagsIdProjection::getTagId).toList();
    }

    @Override
    @Transactional
    public EntityModel<TagDto> deleteTag(String name) {
        var tag = tagRepository.deleteByName(name);
        if (tag.isEmpty()) {
            throw new EntityNotFoundException(getErrorMessage(name),
                HttpErrorCodes.TAG_NOT_FOUND);
        }
        return EntityModel.of(tagDtoAssembler.toModel(tag.get(0)));
    }

    @Override
    public boolean isExist(String name) {
        return tagRepository.existsByName(name);
    }
}
