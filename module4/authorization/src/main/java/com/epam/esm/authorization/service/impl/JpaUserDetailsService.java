package com.epam.esm.authorization.service.impl;

import com.epam.esm.authorization.assembler.PersonDtoAssembler;
import com.epam.esm.authorization.dto.PersonDto;
import com.epam.esm.authorization.dto.PersonRoleDto;
import com.epam.esm.authorization.dto.UpdatePersonRoleDto;
import com.epam.esm.authorization.entity.Person;
import com.epam.esm.authorization.entity.PersonAuthority;
import com.epam.esm.authorization.handler.exception.EntityNotFoundException;
import com.epam.esm.authorization.repository.PersonRepository;
import com.epam.esm.authorization.service.AuthoritiesService;
import com.epam.esm.authorization.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements PersonService {

    private final PersonRepository personRepository;
    private final PersonDtoAssembler personDtoAssembler;
    private final PagedResourcesAssembler<Person> pagedResourcesAssembler;
    private final AuthoritiesService authoritiesService;
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var person = personRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(setEmailNotFoundMessage(email)));
        return User.withUsername(person.getEmail())
            .password(person.getPassword())
            .disabled(!person.getEnabled())
            .authorities(StringUtils.collectionToCommaDelimitedString(
                person.getAuthorities().stream().map(PersonAuthority::getAuthority).collect(Collectors.toSet())))
            .build();
    }

    private String setEmailNotFoundMessage(String email) {
        return "User with email: " + email + " not found";
    }

    @Override
    public void createUser(UserDetails user) {
        var person = new Person();
        toPerson(user, person, true);
        personRepository.save(person);
    }

    private void toPerson(UserDetails user, Person person, boolean changePassword) {
        person.setEmail(user.getUsername());
        if (changePassword) {
            person.setPassword(user.getPassword());
        }
        person.setEnabled(user.isEnabled());
        user.getAuthorities().forEach(grantedAuthority -> {
            isRoleExist(grantedAuthority.getAuthority());
            var personAuthority = new PersonAuthority();
            personAuthority.setAuthority(grantedAuthority.getAuthority());
            person.addPersonAuthority(personAuthority);
        });
    }

    private void isRoleExist(String role) {
        if (!authoritiesService.getAllRoles().contains(role)) {
            throw new EntityNotFoundException("Role " + role + " not found");
        }
    }

    @Override
    public PersonRoleDto createRole(PersonRoleDto personRole) {
        authoritiesService.addRole(personRole.getRole());
        return personRole;
    }

    @Override
    public PagedModel<PersonDto> getAllUsers(int page, int size) {
        return pagedResourcesAssembler.toModel(personRepository.findAll(PageRequest.of(page, size)),
            personDtoAssembler);
    }

    @Override
    public EntityModel<PersonDto> getUserByEmail(String email) {
        return EntityModel.of(personDtoAssembler.toModel(personRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(setEmailNotFoundMessage(email)))));
    }

    @Override
    public List<PersonRoleDto> getAllRoles() {
        return authoritiesService.getAllRoles().stream().map(role -> {
            var personRole = new PersonRoleDto();
            personRole.setRole(role);
            return personRole;
        }).toList();
    }

    @Override
    @Transactional
    public void updateUser(UserDetails user) {
        var person = personRepository.findByEmail(user.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException(setEmailNotFoundMessage(user.getUsername())));
        person.getAuthorities().forEach(person::removePersonAuthority);
        toPerson(user, person, false);
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var currentUser = securityContextHolderStrategy.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no authenticated user found.");
        }
        var email = currentUser.getName();
        var person = personRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(setEmailNotFoundMessage(email)));
        if (!encoder.matches(oldPassword, person.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }
        person.setPassword(encoder.encode(newPassword));
        var authentication = createNewAuthentication(currentUser);
        var context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
    }

    private Authentication createNewAuthentication(Authentication currentAuth) {
        var user = loadUserByUsername(currentAuth.getName());
        var newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user,
            user.getPassword(), user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    @Override
    @Transactional
    public PersonRoleDto updateRole(UpdatePersonRoleDto updatePersonRole) {
        isRoleExist(updatePersonRole.getOldRole());
        authoritiesService.updateRole(updatePersonRole.getOldRole(), updatePersonRole.getNewRole());
        var personRole = new PersonRoleDto();
        personRole.setRole(updatePersonRole.getNewRole());
        return personRole;
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        personRepository.deleteByEmail(email);
    }

    @Override
    @Transactional
    public PersonRoleDto deleteRole(PersonRoleDto personRole) {
        isRoleExist(personRole.getRole());
        authoritiesService.deleteRole(personRole.getRole());
        return personRole;
    }

    @Override
    public boolean userExists(String email) {
        return personRepository.existsByEmail(email);
    }
}
