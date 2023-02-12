package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.aggregation.CountTagsId;
import com.epam.esm.giftcertificates.assembler.TagDtoAssembler;
import com.epam.esm.giftcertificates.repository.TagRepository;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.handler.exception.TagNotFoundException;
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
    return pagedResourcesAssembler.toModel(
        tagRepository.findAll(PageRequest.of(page, size)), tagDtoAssembler);
  }

  @Override
  public EntityModel<TagDto> getTagById(long id) {
    return EntityModel.of(
        tagDtoAssembler.toModel(
            tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id))));
  }

  @Override
  public PagedModel<TagDto> getMostWidelyUsedTagsFromPersonMaxCostRecipe(
      int page, int size, Long id) {
    var countTagsIdFromPersonMaxCostRecipe = tagRepository.findCountTagsInPersonMaxCostRecipe(id);
    var maxCount = getMaxCountTagIdInPersonMaxCostRecipe(countTagsIdFromPersonMaxCostRecipe);
    return pagedResourcesAssembler.toModel(
        tagRepository.findTagByIdIn(
            getTagsIdCompareToMaxCount(countTagsIdFromPersonMaxCostRecipe, maxCount),
            PageRequest.of(page, size)),
        tagDtoAssembler);
  }

  private Long getMaxCountTagIdInPersonMaxCostRecipe(List<CountTagsId> countTagsIdList) {
    return countTagsIdList.stream()
        .mapToLong(CountTagsId::getTagCount)
        .max()
        .orElseThrow(
            () ->
                new TagNotFoundException("Most widely used tag in recipe with max cost not found"));
  }

  private List<Long> getTagsIdCompareToMaxCount(List<CountTagsId> countTagsIdList, Long maxCount) {
    return countTagsIdList.stream()
        .filter(countTagsId -> Objects.equals(countTagsId.getTagCount(), maxCount))
        .map(CountTagsId::getTagId)
        .toList();
  }

  @Override
  public TagDto getTagByName(String name) {
    return entityDtoMapper.tagToTagDto(
        tagRepository
            .findTagByName(name)
            .orElseGet(
                () -> {
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
