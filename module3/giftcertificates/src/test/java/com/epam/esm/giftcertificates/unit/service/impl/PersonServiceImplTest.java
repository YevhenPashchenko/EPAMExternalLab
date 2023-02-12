package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.PersonDtoAssembler;
import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.entity.Person;
import com.epam.esm.giftcertificates.repository.PersonRepository;
import com.epam.esm.giftcertificates.handler.exception.PersonNotFoundException;
import com.epam.esm.giftcertificates.service.PersonService;
import com.epam.esm.giftcertificates.service.impl.PersonServiceImpl;
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

class PersonServiceImplTest {

  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final PersonDtoAssembler personDtoAssembler = mock(PersonDtoAssembler.class);
  private final PagedResourcesAssembler<Person> pagedResourcesAssembler =
      mock(PagedResourcesAssembler.class);
  private final PersonService personService =
      new PersonServiceImpl(personRepository, personDtoAssembler, pagedResourcesAssembler);

  @Test
  void getAllPersons_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // WHEN
    personService.getAllPersons(0, 2);

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(personRepository.findAll(PageRequest.of(0, 2)), personDtoAssembler);
  }

  @Test
  void getPersonDtoById_shouldReturnPersonDtoEntityModel_whenPersonWithThisIdExist() {
    // GIVEN
    given(personRepository.findById(anyLong())).willReturn(Optional.of(new Person()));
    given(personDtoAssembler.toModel(any(Person.class))).willReturn(new PersonDto());

    // WHEN
    var result = personService.getPersonDtoById(0L);

    // THEN
    assertEquals(EntityModel.of(new PersonDto()), result);
  }

  @Test
  void getPersonDtoById_shouldThrowUserNotFoundException_whenPersonWithThisIdNotExist() {
    // GIVEN
    given(personRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(PersonNotFoundException.class, () -> personService.getPersonDtoById(0L));
  }

  @Test
  void getPersonById_shouldReturnPerson_whenPersonWithThisIdExist() {
    // GIVEN
    given(personRepository.findById(anyLong())).willReturn(Optional.of(new Person()));

    // WHEN
    var result = personService.getPersonById(0L);

    // THEN
    assertEquals(new Person(), result);
  }

  @Test
  void getPersonById_shouldThrowUserNotFoundException_whenPersonWithThisIdNotExist() {
    // GIVEN
    given(personRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(PersonNotFoundException.class, () -> personService.getPersonById(0L));
  }
}
