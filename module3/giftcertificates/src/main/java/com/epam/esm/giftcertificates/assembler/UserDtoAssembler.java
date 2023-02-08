package com.epam.esm.giftcertificates.assembler;

import com.epam.esm.giftcertificates.controller.UserController;
import com.epam.esm.giftcertificates.dto.UserDto;
import com.epam.esm.giftcertificates.entity.User;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDtoAssembler extends RepresentationModelAssemblerSupport<User, UserDto> {

  private final EntityDtoMapper entityDtoMapper;

  public UserDtoAssembler(EntityDtoMapper entityDtoMapper) {
    super(UserController.class, UserDto.class);
    this.entityDtoMapper = entityDtoMapper;
  }

  @Override
  public UserDto toModel(@NonNull User user) {
    var userDto = entityDtoMapper.userToUserDto(user);
    addSelfLinkToUserDto(userDto);
    return userDto;
  }

  private void addSelfLinkToUserDto(UserDto userDto) {
    userDto.add(
        linkTo(methodOn(UserController.class).getUserDtoById(userDto.getId())).withSelfRel());
  }
}
