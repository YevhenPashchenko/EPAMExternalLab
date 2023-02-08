package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.UserDtoAssembler;
import com.epam.esm.giftcertificates.repository.UserRepository;
import com.epam.esm.giftcertificates.dto.UserDto;
import com.epam.esm.giftcertificates.entity.User;
import com.epam.esm.giftcertificates.handler.exception.UserNotFoundException;
import com.epam.esm.giftcertificates.service.UserService;
import com.epam.esm.giftcertificates.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class UserServiceImplTest {

  private final UserRepository userRepository = mock(UserRepository.class);
  private final UserDtoAssembler userDtoAssembler = mock(UserDtoAssembler.class);
  private final PagedResourcesAssembler<User> pagedResourcesAssembler =
      mock(PagedResourcesAssembler.class);
  private final UserService userService =
      new UserServiceImpl(userRepository, userDtoAssembler, pagedResourcesAssembler);

  @Test
  void getAllUserDto_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // WHEN
    userService.getAllUserDto(0, 2);

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(userRepository.findAll(PageRequest.of(0, 2)), userDtoAssembler);
  }

  @Test
  void getUserDtoById_shouldReturnUserDtoEntityModel_whenUserWithThisIdExist() {
    // GIVEN
    given(userRepository.findById(anyLong())).willReturn(Optional.of(new User()));
    given(userDtoAssembler.toModel(any(User.class))).willReturn(new UserDto());

    // WHEN
    var result = userService.getUserDtoById(0L);

    // THEN
    assertEquals(EntityModel.of(new UserDto()), result);
  }

  @Test
  void getUserDtoById_shouldThrowUserNotFoundException_whenUserWithThisIdNotExist() {
    // GIVEN
    given(userRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(UserNotFoundException.class, () -> userService.getUserDtoById(0L));
  }
}
