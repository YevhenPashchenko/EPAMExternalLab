package com.epam.esm.authorization.repository;

import com.epam.esm.authorization.entity.PersonAuthority;
import com.epam.esm.authorization.entity.projection.RoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonAuthorityRepository extends JpaRepository<PersonAuthority, Long> {

    @Query("SELECT DISTINCT p.authority FROM com.epam.esm.authorization.entity.PersonAuthority p")
    List<RoleProjection> findRoles();

    @Modifying
    @Query("UPDATE com.epam.esm.authorization.entity.PersonAuthority p SET p.authority = :newRole WHERE p.authority = :oldRole")
    void updateRole(String oldRole, String newRole);

    void deleteByAuthority(String authority);
}
