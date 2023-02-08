package com.epam.esm.giftcertificates.assembler;

import com.epam.esm.giftcertificates.controller.TagController;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagDtoAssembler extends RepresentationModelAssemblerSupport<Tag, TagDto> {

  private final EntityDtoMapper entityDtoMapper;

  public TagDtoAssembler(EntityDtoMapper entityDtoMapper) {
    super(TagController.class, TagDto.class);
    this.entityDtoMapper = entityDtoMapper;
  }

  @Override
  public TagDto toModel(@NonNull Tag tag) {
    var tagDto = entityDtoMapper.tagToTagDto(tag);
    addSelfLinkToTagDto(tagDto);
    return tagDto;
  }

  private void addSelfLinkToTagDto(TagDto tagDto) {
    tagDto.add(linkTo(methodOn(TagController.class).getTagDtoById(tagDto.getId())).withSelfRel());
  }
}
