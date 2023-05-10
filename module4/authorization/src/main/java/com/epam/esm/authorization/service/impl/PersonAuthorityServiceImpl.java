package com.epam.esm.authorization.service.impl;

import com.epam.esm.authorization.entity.projection.RoleProjection;
import com.epam.esm.authorization.repository.PersonAuthorityRepository;
import com.epam.esm.authorization.service.PersonAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonAuthorityServiceImpl implements PersonAuthorityService {

    private final PersonAuthorityRepository personAuthorityRepository;

    @Override
    public Set<String> getRoles() {
        return personAuthorityRepository.findRoles().stream().map(RoleProjection::getRole).collect(Collectors.toSet());
    }

    @Override
    public void updateRole(String oldRole, String newRole) {
        personAuthorityRepository.updateRole(oldRole, newRole);
    }

    @Override
    public void deleteRole(String role) {
        personAuthorityRepository.deleteByAuthority(role);
    }
}
