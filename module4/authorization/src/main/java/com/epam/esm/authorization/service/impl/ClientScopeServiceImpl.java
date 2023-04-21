package com.epam.esm.authorization.service.impl;

import com.epam.esm.authorization.entity.projection.ScopeProjection;
import com.epam.esm.authorization.repository.ClientScopeRepository;
import com.epam.esm.authorization.service.ClientScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientScopeServiceImpl implements ClientScopeService {

    private final ClientScopeRepository clientScopeRepository;

    @Override
    public Set<String> getScopes() {
        return clientScopeRepository.findScopes().stream().map(ScopeProjection::getScope).collect(Collectors.toSet());
    }

    @Override
    public void updateScope(String oldScope, String newScope) {
        clientScopeRepository.updateScope(oldScope, newScope);
    }

    @Override
    public void deleteScope(String scope) {
        clientScopeRepository.deleteByScope(scope);
    }
}
