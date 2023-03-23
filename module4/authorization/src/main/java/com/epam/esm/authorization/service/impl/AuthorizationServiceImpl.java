package com.epam.esm.authorization.service.impl;

import com.epam.esm.authorization.dto.AuthorizationCodeDto;
import com.epam.esm.authorization.dto.LoginDto;
import com.epam.esm.authorization.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    private final AuthorizationServerSettings authorizationServerSettings;

    @Override
    public AuthorizationCodeDto login(LoginDto login) {
        var authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(), login.getPassword()));
        setAuthorizationServerContext();
        var authorizationCodeProvider =
            new OAuth2AuthorizationCodeRequestAuthenticationProvider(registeredClientRepository, authorizationService,
                authorizationConsentService);
        var authorizationToken = (OAuth2AuthorizationCodeRequestAuthenticationToken) authorizationCodeProvider.authenticate(
            new OAuth2AuthorizationCodeRequestAuthenticationToken(
                authorizationServerSettings.getIssuer() + "/login", login.getClientId(), authentication,
                login.getRedirectUri(), null, login.getScopes(), null));
        var code = new AuthorizationCodeDto();
        code.setCode(Objects.requireNonNull(authorizationToken.getAuthorizationCode()).getTokenValue());
        return code;
    }

    private void setAuthorizationServerContext() {
        AuthorizationServerContextHolder.setContext(new AuthorizationServerContext() {
            @Override
            public String getIssuer() {
                return authorizationServerSettings.getIssuer();
            }

            @Override
            public AuthorizationServerSettings getAuthorizationServerSettings() {
                return authorizationServerSettings;
            }
        });
    }
}
