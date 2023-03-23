package com.epam.esm.authorization.unit.service.impl;

import com.epam.esm.authorization.assembler.PersonDtoAssembler;
import com.epam.esm.authorization.entity.Person;
import com.epam.esm.authorization.repository.PersonRepository;
import com.epam.esm.authorization.service.impl.JpaUserDetailsService;
import com.epam.esm.authorization.util.TestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class JpaUserDetailsServiceTest {

    private final static int PAGE = 0;
    private final static int SIZE = 2;
    private final static String NEW_PASSWORD = "newPassword";

    private final PersonRepository personRepository = mock(PersonRepository.class);
    private final PersonDtoAssembler personDtoAssembler = mock(PersonDtoAssembler.class);
    @SuppressWarnings("unchecked")
    private final PagedResourcesAssembler<Person> pagedResourcesAssembler
        = mock(PagedResourcesAssembler.class);
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();
    private final JpaUserDetailsService jpaUserDetailsService =
        new JpaUserDetailsService(personRepository, personDtoAssembler, pagedResourcesAssembler);

    @BeforeEach
    public void clearContext() {
        securityContextHolderStrategy.clearContext();
    }

    @Test
    void loadUserByUsername_shouldReturnUser_whenUserWithThisEmailExist() {
        // GIVEN
        var person = TestEntityFactory.createDefaultPerson();
        given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person));

        // WHEN
        var result = jpaUserDetailsService.loadUserByUsername(person.getEmail());

        // THEN
        assertThat(result).isEqualTo(TestEntityFactory.createDefaultUser());
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserWithThisEmailNotExist() {
        // GIVEN
        var person = TestEntityFactory.createDefaultPerson();
        given(personRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(UsernameNotFoundException.class,
            () -> jpaUserDetailsService.loadUserByUsername(person.getEmail()));
    }

    @Test
    void createUser_shouldCallsPersonRepositorySave_whenExecutedNormally() {
        // WHEN
        jpaUserDetailsService.createUser(TestEntityFactory.createDefaultUser());

        // THEN
        then(personRepository).should(atLeastOnce()).save(any(Person.class));
    }

    @Test
    void getAllUsers_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
        // WHEN
        jpaUserDetailsService.getAllUsers(PAGE, SIZE);

        // THEN
        then(pagedResourcesAssembler).should(atLeastOnce())
            .toModel(personRepository.findAll(PageRequest.of(PAGE, SIZE)), personDtoAssembler);
    }

    @Test
    void getUserByEmail_shouldReturnPerson_whenUserWithThisEmailExist() {
        // GIVEN
        var person = TestEntityFactory.createDefaultPerson();
        var personDto = TestEntityFactory.createDefaultPersonDto();
        given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person));
        given(personDtoAssembler.toModel(any(Person.class))).willReturn(personDto);

        // WHEN
        var result = jpaUserDetailsService.getUserByEmail(person.getEmail());

        // THEN
        assertThat(result).isEqualTo(EntityModel.of(personDto));
    }

    @Test
    void getUserByEmail_shouldThrowUsernameNotFoundException_whenUserWithThisEmailNotExist() {
        // GIVEN
        given(personRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(UsernameNotFoundException.class, () -> jpaUserDetailsService.getUserByEmail(null));
    }

    @Test
    void updateUser_shouldThrowUsernameNotFoundException_whenUserWithThisEmailNotExist() {
        // GIVEN
        given(personRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // THEN
        assertThrows(UsernameNotFoundException.class,
            () -> jpaUserDetailsService.updateUser(TestEntityFactory.createDefaultUser()));
    }

    @Test
    void changePassword_shouldChangeAuthentication_whenExecutedNormally() {
        // GIVEN
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var user = TestEntityFactory.createDefaultUser();
        var person = TestEntityFactory.createDefaultPerson();
        var oldPassword = person.getPassword();
        person.setPassword(encoder.encode(person.getPassword()));
        securityContextHolderStrategy.getContext()
            .setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), user.getAuthorities()));
        var authentication = securityContextHolderStrategy.getContext().getAuthentication();
        given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person));

        // WHEN
        jpaUserDetailsService.changePassword(oldPassword, NEW_PASSWORD);

        // THEN
        assertThat(authentication).isNotEqualTo(securityContextHolderStrategy.getContext().getAuthentication());
    }

    @Test
    void changePassword_shouldThrowAccessDeniedException_whenAuthenticatedUserNotFound() {
        // THEN
        assertThrows(AccessDeniedException.class,
            () -> jpaUserDetailsService.changePassword(NEW_PASSWORD, NEW_PASSWORD));
    }

    @Test
    void changePassword_shouldThrowUsernameNotFoundException_whenAuthenticatedUserWithThisEmailNotExist() {
        // GIVEN
        var user = TestEntityFactory.createDefaultUser();
        securityContextHolderStrategy.getContext()
            .setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), user.getAuthorities()));
        given(personRepository.findByEmail(user.getUsername())).willReturn(Optional.empty());

        // THEN
        assertThrows(UsernameNotFoundException.class,
            () -> jpaUserDetailsService.changePassword(user.getPassword(), NEW_PASSWORD));
    }

    @Test
    void changePassword_shouldThrowBadCredentialsException_whenOldPasswordNotValid() {
        // GIVEN
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var user = TestEntityFactory.createDefaultUser();
        var person = TestEntityFactory.createDefaultPerson();
        person.setPassword(encoder.encode(person.getPassword()));
        securityContextHolderStrategy.getContext()
            .setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), user.getAuthorities()));
        given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person));

        // THEN
        assertThrows(BadCredentialsException.class,
            () -> jpaUserDetailsService.changePassword(NEW_PASSWORD, NEW_PASSWORD));
    }
}
