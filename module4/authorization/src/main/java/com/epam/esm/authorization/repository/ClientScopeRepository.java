package com.epam.esm.authorization.repository;

import com.epam.esm.authorization.entity.ClientScope;
import com.epam.esm.authorization.entity.projection.ScopeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientScopeRepository extends JpaRepository<ClientScope, Long> {

    @Query("SELECT DISTINCT c.scope FROM com.epam.esm.authorization.entity.ClientScope c")
    List<ScopeProjection> findScopes();

    @Modifying
    @Query("UPDATE com.epam.esm.authorization.entity.ClientScope c SET c.scope = :newScope WHERE c.scope = :oldScope")
    void updateScope(String oldScope, String newScope);

    void deleteByScope(String scope);
}
