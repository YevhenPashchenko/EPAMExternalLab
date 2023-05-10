package com.epam.esm.authorization.unit.service.impl;

import com.epam.esm.authorization.repository.ClientScopeRepository;
import com.epam.esm.authorization.service.impl.ClientScopeServiceImpl;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class ClientScopeServiceImplTest {

    private static final String SCOPE = "scope";
    private final ClientScopeRepository clientScopeRepository = mock(ClientScopeRepository.class);
    private final ClientScopeServiceImpl clientScopeService = new ClientScopeServiceImpl(clientScopeRepository);

    @Test
    void getScopes_shouldCallsClientScopeRepositoryFindScopes_whenExecutedNormally() {
        // WHEN
        clientScopeService.getScopes();

        // THEN
        then(clientScopeRepository).should(atLeastOnce()).findScopes();
    }

    @Test
    void updateScope_shouldCallsClientScopeRepositoryUpdateScope_whenExecutedNormally() {
        // GIVEN
        var newScope = "newScope";

        // WHEN
        clientScopeService.updateScope(SCOPE, newScope);

        // THEN
        then(clientScopeRepository).should(atLeastOnce()).updateScope(SCOPE, newScope);
    }

    @Test
    void deleteScope_shouldCallsClientScopeRepositoryDeleteByScope_whenExecutedNormally() {
        // WHEN
        clientScopeService.deleteScope(SCOPE);

        // THEN
        then(clientScopeRepository).should(atLeastOnce()).deleteByScope(SCOPE);
    }
}
