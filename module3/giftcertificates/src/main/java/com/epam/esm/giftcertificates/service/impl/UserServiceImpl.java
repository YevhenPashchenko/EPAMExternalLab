package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.UserDtoAssembler;
import com.epam.esm.giftcertificates.repository.UserRepository;
import com.epam.esm.giftcertificates.dto.UserDto;
import com.epam.esm.giftcertificates.entity.User;
import com.epam.esm.giftcertificates.handler.exception.UserNotFoundException;
import com.epam.esm.giftcertificates.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserDtoAssembler userDtoAssembler;
  private final PagedResourcesAssembler<User> pagedResourcesAssembler;

  @Override
  public PagedModel<UserDto> getAllUserDto(int page, int size) {
    return pagedResourcesAssembler.toModel(
        userRepository.findAll(PageRequest.of(page, size)), userDtoAssembler);
  }

  @Override
  public EntityModel<UserDto> getUserDtoById(Long id) {
    return EntityModel.of(userDtoAssembler.toModel(getUserById(id)));
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }
}
