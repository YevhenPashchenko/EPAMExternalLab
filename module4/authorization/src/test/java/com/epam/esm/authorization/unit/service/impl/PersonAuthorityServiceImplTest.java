package com.epam.esm.authorization.unit.service.impl;

import com.epam.esm.authorization.repository.PersonAuthorityRepository;
import com.epam.esm.authorization.service.impl.PersonAuthorityServiceImpl;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class PersonAuthorityServiceImplTest {

    private final static String ROLE = "role";
    private final PersonAuthorityRepository personAuthorityRepository = mock(PersonAuthorityRepository.class);
    private final PersonAuthorityServiceImpl personAuthorityService =
        new PersonAuthorityServiceImpl(personAuthorityRepository);

    @Test
    void getRoles_shouldCallsPersonAuthorityRepositoryFindRoles_whenExecutedNormally() {
        // WHEN
        personAuthorityService.getRoles();

        // THEN
        then(personAuthorityRepository).should(atLeastOnce()).findRoles();
    }

    @Test
    void updateRole_shouldCallsPersonAuthorityRepositoryUpdateRole_whenExecutedNormally() {
        // GIVEN
        var newRole = "newRole";

        // WHEN
        personAuthorityService.updateRole(ROLE, newRole);

        // THEN
        then(personAuthorityRepository).should(atLeastOnce()).updateRole(ROLE, newRole);
    }

    @Test
    void deleteRole_shouldCallsPersonAuthorityRepositoryDeleteByAuthority_whenExecutedNormally() {
        // WHEN
        personAuthorityService.deleteRole(ROLE);

        // THEN
        then(personAuthorityRepository).should(atLeastOnce()).deleteByAuthority(ROLE);
    }
}
