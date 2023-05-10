package com.epam.esm.authorization.service.impl;

import com.epam.esm.authorization.dto.AuthorizationCodeDto;
import com.epam.esm.authorization.dto.LoginDto;
import com.epam.esm.authorization.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final String DEFAULT_PERSON_EMAIL = "person@mail.com";
    private static final String DEFAULT_PERSON_PASSWORD = "password";
    private static final String LOGIN_URL = "/login";
    private static final String PERSONS_READ_SCOPE = "persons.read";
    private final AuthenticationManager authenticationManager;
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    private final AuthorizationServerSettings authorizationServerSettings;
    private Authentication defaultAuthentication = null;
    private OAuth2AuthorizationCodeRequestAuthenticationProvider authorizationCodeProvider = null;

    @Override
    public AuthorizationCodeDto getTokenForLogin(String clientId) {
        setAuthorizationServerContext();
        if (defaultAuthentication == null) {
            setDefaultAuthentication();
        }
        var authenticationToken =
            (OAuth2AuthorizationCodeRequestAuthenticationToken) getAuthentication(
                authorizationServerSettings.getIssuer() + LOGIN_URL,
                clientId, defaultAuthentication, authorizationServerSettings.getIssuer() + LOGIN_URL, null,
                Set.of(PERSONS_READ_SCOPE), null);
        var code = new AuthorizationCodeDto();
        code.setCode(Objects.requireNonNull(authenticationToken.getAuthorizationCode()).getTokenValue());
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

    private void setDefaultAuthentication() {
        defaultAuthentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(DEFAULT_PERSON_EMAIL, DEFAULT_PERSON_PASSWORD));
    }

    private Authentication getAuthentication(String authorizationUri, String clientId, Authentication principal,
        String redirectUri, String state, Set<String> scopes, Map<String, Object> additionalParameters) {
        if (authorizationCodeProvider == null) {
            setAuthorizationCodeProvider();
        }
        return authorizationCodeProvider.authenticate(
            new OAuth2AuthorizationCodeRequestAuthenticationToken(authorizationUri, clientId, principal, redirectUri,
                state, scopes, additionalParameters));
    }

    private void setAuthorizationCodeProvider() {
        authorizationCodeProvider =
            new OAuth2AuthorizationCodeRequestAuthenticationProvider(registeredClientRepository, authorizationService,
                authorizationConsentService);
    }

    @Override
    public AuthorizationCodeDto login(LoginDto login) {
        var authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(), login.getPassword()));
        setAuthorizationServerContext();
        var authenticationToken = (OAuth2AuthorizationCodeRequestAuthenticationToken) getAuthentication(
            authorizationServerSettings.getIssuer() + LOGIN_URL, login.getClientId(), authentication,
            login.getRedirectUri(), null, login.getScopes(), null);
        var code = new AuthorizationCodeDto();
        code.setCode(Objects.requireNonNull(authenticationToken.getAuthorizationCode()).getTokenValue());
        return code;
    }
}
