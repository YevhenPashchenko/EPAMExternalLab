package com.epam.esm.authorization.controller;

import com.epam.esm.authorization.dto.ChangePersonPasswordDto;
import com.epam.esm.authorization.dto.PersonEmailDto;
import com.epam.esm.authorization.dto.CreatePersonDto;
import com.epam.esm.authorization.dto.PersonDto;
import com.epam.esm.authorization.dto.PersonRoleDto;
import com.epam.esm.authorization.dto.UpdatePersonRoleDto;
import com.epam.esm.authorization.service.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @PreAuthorize("hasAuthority(@authoritiesServiceImpl.personsWriteScope)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody CreatePersonDto person) {
        personService.createUser(
            User.withUsername(person.getEmail())
                .password(person.getPassword())
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .roles("user")
                .build()
        );
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonRoleDto createRole(@Valid @RequestBody PersonRoleDto personRole) {
        return personService.createRole(personRole);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.personsReadScope)")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<PersonDto> getAll(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0") int page,
        @RequestParam(defaultValue = "20") @Range(min = 1, max = 100, message = "size must be between 1 and 100") int size) {
        return personService.getAllUsers(page, size);
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.personsReadScope)")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<PersonDto> getByEmail(@Valid @RequestBody PersonEmailDto person) {
        return personService.getUserByEmail(person.getEmail());
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @GetMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonRoleDto> getAllRoles() {
        return personService.getAllRoles();
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.personsWriteScope)")
    public void update(@Valid @RequestBody PersonDto person) {
        personService.updateUser(
            User.withUsername(person.getEmail())
                .password("default")
                .disabled(!person.getEnabled())
                .roles(StringUtils.collectionToCommaDelimitedString(person.getAuthorities()))
                .build()
        );
    }

    @PreAuthorize("hasAuthority(@authoritiesServiceImpl.personsWriteScope)")
    @PatchMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@Valid @RequestBody ChangePersonPasswordDto password) {
        personService.changePassword(password.getOldPassword(), password.getNewPassword());
    }

    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles)")
    @PatchMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonRoleDto updateRole(@Valid @RequestBody UpdatePersonRoleDto updatePersonRole) {
        return personService.updateRole(updatePersonRole);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole(@authoritiesServiceImpl.superRoles) && hasAuthority(@authoritiesServiceImpl.personsWriteScope)")
    public void delete(@Valid @RequestBody PersonEmailDto person) {
        personService.deleteUser(person.getEmail());
    }

    @PreAuthorize("hasAuthority(@authoritiesServiceImpl.superRoles)")
    @DeleteMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonRoleDto deleteRole(@Valid @RequestBody PersonRoleDto personRole) {
        return personService.deleteRole(personRole);
    }
}
