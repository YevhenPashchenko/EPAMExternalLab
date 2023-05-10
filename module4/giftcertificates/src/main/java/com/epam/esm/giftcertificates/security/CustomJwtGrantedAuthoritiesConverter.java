package com.epam.esm.giftcertificates.security;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Collection<String> AUTHORITIES_CLAIM_NAMES = Arrays.asList("scope", "roles");

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        var grantedAuthorities = new ArrayList<GrantedAuthority>();
        getAuthorities(jwt).forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority(authority)));
        return grantedAuthorities;
    }

    private Collection<String> getAuthorities(Jwt jwt) {
        var authorities = new ArrayList<String>();
        AUTHORITIES_CLAIM_NAMES.forEach(claimName -> {
            var authoritiesObject = jwt.getClaim(claimName);
            var authoritiesList = new ArrayList<String>();
            if (authoritiesObject instanceof String authoritiesString && StringUtils.hasText(authoritiesString)) {
                authoritiesList.addAll(Arrays.asList(authoritiesString.split(" ")));
            }
            if (authoritiesObject instanceof Collection) {
                authoritiesList.addAll(castAuthoritiesToCollection(authoritiesObject));
            }
            if ("scope".equals(claimName)) {
                addPrefixToAuthority(authoritiesList);
            }
            authorities.addAll(authoritiesList);
        });
        return !authorities.isEmpty() ? authorities : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Collection<String> castAuthoritiesToCollection(Object authoritiesCollection) {
        return (Collection<String>) authoritiesCollection;
    }

    private void addPrefixToAuthority(ArrayList<String> authoritiesList) {
        for (int i = 0; i < authoritiesList.size(); i++) {
            authoritiesList.set(i, "SCOPE_" + authoritiesList.get(i));
        }
    }
}
