package com.epam.esm.authorization.unit.service.impl;

import com.epam.esm.authorization.service.impl.AuthorizationServiceImpl;
import com.epam.esm.authorization.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthorizationServiceImplTest {

    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final RegisteredClientRepository registeredClientRepository = mock(RegisteredClientRepository.class);
    private final OAuth2AuthorizationService authorizationService = mock(OAuth2AuthorizationService.class);
    private final OAuth2AuthorizationConsentService authorizationConsentService =
        mock(OAuth2AuthorizationConsentService.class);
    private final AuthorizationServerSettings authorizationServerSettings =
        AuthorizationServerSettings.builder().issuer("http://127.0.0.1:9000").build();
    private final AuthorizationServiceImpl authorizationServiceImpl =
        new AuthorizationServiceImpl(authenticationManager, registeredClientRepository, authorizationService,
            authorizationConsentService, authorizationServerSettings);

    @Test
    void getTokenForLogin_shouldReturnAuthorizationCode_whenExecutedNormally() {
        // GIVEN
        var user = TestEntityFactory.createDefaultUser();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(
            new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
        given(registeredClientRepository.findByClientId(anyString())).willReturn(client);

        // WHEN
        var result = authorizationServiceImpl.getTokenForLogin(client.getClientId());

        // THEN
        assertThat(result).isNotNull();
    }

    @Test
    void login_shouldReturnAuthorizationCode_whenExecutedNormally() {
        // GIVEN
        var login = TestEntityFactory.createDefaultLoginDto();
        var user = TestEntityFactory.createDefaultUser();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(
            new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
        given(registeredClientRepository.findByClientId(anyString())).willReturn(client);

        // WHEN
        var result = authorizationServiceImpl.login(login);

        // THEN
        assertThat(result).isNotNull();
    }
}
