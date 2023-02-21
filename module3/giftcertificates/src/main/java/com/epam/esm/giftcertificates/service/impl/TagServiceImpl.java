package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.constant.HttpErrorCodes;
import com.epam.esm.giftcertificates.entity.projection.TagsCountToTagsIdProjection;
import com.epam.esm.giftcertificates.assembler.TagDtoAssembler;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.repository.TagRepository;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final TagDtoAssembler tagDtoAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;

    @Override
    public EntityModel<TagDto> createTag(TagDto tagDto) {
        var tag = entityDtoMapper.tagDtoToTag(tagDto);
        saveTag(tag);
        return EntityModel.of(tagDtoAssembler.toModel(tag));
    }

    private Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public PagedModel<TagDto> getAllTags(int page, int size) {
        return pagedResourcesAssembler.toModel(tagRepository.findAll(PageRequest.of(page, size)), tagDtoAssembler);
    }

    @Override
    public EntityModel<TagDto> getTagById(long id) {
        return EntityModel.of(tagDtoAssembler.toModel(tagRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Requested resource not found (id = " + id + ")",
                HttpErrorCodes.TAG_NOT_FOUND))));
    }

    @Override
    public PagedModel<TagDto> getMostWidelyUsedTagsFromPersonMaxCostReceipt(int page, int size, Long id) {
        var countTagsIdFromPersonMaxCostReceipt = tagRepository.findCountTagsInPersonMaxCostReceipt(id);
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
    public TagDto getTagByName(String name) {
        return entityDtoMapper.tagToTagDto(tagRepository.findTagByName(name).orElseGet(() -> {
            var tag = new Tag();
            tag.setName(name);
            return saveTag(tag);
        }));
    }

    @Override
    public Set<Tag> getTagsByGiftCertificateId(Long id) {
        return tagRepository.findTagsByGiftCertificatesId(id);
    }

    @Override
    public void deleteTag(long id) {
        tagRepository.deleteById(id);
    }
}
