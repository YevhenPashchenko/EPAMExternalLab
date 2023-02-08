package com.epam.esm.giftcertificates.service.impl;

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
    tagRepository.save(tag);
    return EntityModel.of(tagDtoAssembler.toModel(tag));
  }

  @Override
  public PagedModel<TagDto> getAllTagDto(int page, int size) {
    return pagedResourcesAssembler.toModel(
        tagRepository.findAll(PageRequest.of(page, size)), tagDtoAssembler);
  }

  @Override
  public EntityModel<TagDto> getTagDtoById(long id) {
    return EntityModel.of(
        tagDtoAssembler.toModel(
            tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id))));
  }

  @Override
  public TagDto getTagDtoByName(String name) {
    return entityDtoMapper.tagToTagDto(
        tagRepository
            .findTagByName(name)
            .orElseGet(
                () -> {
                  var tag = new Tag();
                  tag.setName(name);
                  return tagRepository.save(tag);
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
