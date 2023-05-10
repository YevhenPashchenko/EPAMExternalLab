package com.epam.esm.authorization.unit.service.impl;

import com.epam.esm.authorization.entity.Authorization;
import com.epam.esm.authorization.repository.AuthorizationRepository;
import com.epam.esm.authorization.service.ClientService;
import com.epam.esm.authorization.service.impl.JpaOAuth2AuthorizationService;
import com.epam.esm.authorization.util.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class JpaOAuth2AuthorizationServiceTest {

    private final AuthorizationRepository authorizationRepository = mock(AuthorizationRepository.class);
    private final ClientService clientService = mock(ClientService.class);
    private final JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService =
        new JpaOAuth2AuthorizationService(authorizationRepository, clientService);

    @Test
    void save_shouldCallsAuthorizationRepositorySave_whenExecutedNormally() {
        // GIVEN
        var authorization = TestEntityFactory.createDefaultOAuth2Authorization();

        // WHEN
        jpaOAuth2AuthorizationService.save(authorization);

        // THEN
        then(authorizationRepository).should(atLeastOnce()).save(any(Authorization.class));
    }

    @Test
    void remove_shouldCallsAuthorizationRepositoryDeleteById_whenExecutedNormally() {
        // GIVEN
        var authorization = TestEntityFactory.createDefaultOAuth2Authorization();

        // WHEN
        jpaOAuth2AuthorizationService.remove(authorization);

        // THEN
        then(authorizationRepository).should(atLeastOnce()).deleteById(anyString());
    }

    @Test
    void findById_shouldReturnOAuth2Authorization_whenAuthorizationWithThisIdExist() {
        // GIVEN
        var auth2Authorization = TestEntityFactory.createDefaultOAuth2Authorization();
        var authorization = TestEntityFactory.createDefaultAuthorization();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authorizationRepository.findById(anyString())).willReturn(Optional.of(authorization));
        given(clientService.findById(anyString())).willReturn(client);

        // WHEN
        var result = jpaOAuth2AuthorizationService.findById(auth2Authorization.getId());

        // THEN
        assertThat(result).hasSameClassAs(auth2Authorization);
    }

    @Test
    void findById_shouldReturnNull_whenAuthorizationWithThisIdNotExist() {
        // GIVEN
        var auth2Authorization = TestEntityFactory.createDefaultOAuth2Authorization();
        given(authorizationRepository.findById(anyString())).willReturn(Optional.empty());

        // WHEN
        var result = jpaOAuth2AuthorizationService.findById(auth2Authorization.getId());

        // THEN
        assertThat(result).isNull();
    }

    @Test
    void findByToken_shouldCallsAuthorizationRepositoryFindByAnyTokenType_whenTokenTypeIsNull() {
        // GIVEN
        var auth2Authorization = TestEntityFactory.createDefaultOAuth2Authorization();
        var authorization = TestEntityFactory.createDefaultAuthorization();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(
            anyString())).willReturn(Optional.of(authorization));
        given(clientService.findById(anyString())).willReturn(client);

        // WHEN
        var result = jpaOAuth2AuthorizationService.findByToken("token", null);

        // THEN
        assertThat(result).hasSameClassAs(auth2Authorization);
        then(authorizationRepository).should(atLeastOnce())
            .findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(anyString());
    }

    @Test
    void findByToken_shouldCallsAuthorizationRepositoryFindByState_whenTokenTypeIsState() {
        // GIVEN
        var auth2Authorization = TestEntityFactory.createDefaultOAuth2Authorization();
        var authorization = TestEntityFactory.createDefaultAuthorization();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authorizationRepository.findByState(anyString())).willReturn(Optional.of(authorization));
        given(clientService.findById(anyString())).willReturn(client);

        // WHEN
        var result = jpaOAuth2AuthorizationService.findByToken("token", new OAuth2TokenType("state"));

        // THEN
        assertThat(result).hasSameClassAs(auth2Authorization);
        then(authorizationRepository).should(atLeastOnce()).findByState(anyString());
    }

    @Test
    void findByToken_shouldCallsAuthorizationRepositoryFindByAuthorizationCodeValue_whenTokenTypeIsAuthorizationCode() {
        // GIVEN
        var auth2Authorization = TestEntityFactory.createDefaultOAuth2Authorization();
        var authorization = TestEntityFactory.createDefaultAuthorization();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authorizationRepository.findByAuthorizationCodeValue(anyString())).willReturn(Optional.of(authorization));
        given(clientService.findById(anyString())).willReturn(client);

        // WHEN
        var result = jpaOAuth2AuthorizationService.findByToken("token", new OAuth2TokenType("code"));

        // THEN
        assertThat(result).hasSameClassAs(auth2Authorization);
        then(authorizationRepository).should(atLeastOnce()).findByAuthorizationCodeValue(anyString());
    }

    @Test
    void findByToken_shouldCallsAuthorizationRepositoryFindByAccessTokenValue_whenTokenTypeIsAccessToken() {
        // GIVEN
        var auth2Authorization = TestEntityFactory.createDefaultOAuth2Authorization();
        var authorization = TestEntityFactory.createDefaultAuthorization();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authorizationRepository.findByAccessTokenValue(anyString())).willReturn(Optional.of(authorization));
        given(clientService.findById(anyString())).willReturn(client);

        // WHEN
        var result = jpaOAuth2AuthorizationService.findByToken("token", OAuth2TokenType.ACCESS_TOKEN);

        // THEN
        assertThat(result).hasSameClassAs(auth2Authorization);
        then(authorizationRepository).should(atLeastOnce()).findByAccessTokenValue(anyString());
    }

    @Test
    void findByToken_shouldCallsAuthorizationRepositoryFindByRefreshTokenValue_whenTokenTypeIsRefreshToken() {
        // GIVEN
        var auth2Authorization = TestEntityFactory.createDefaultOAuth2Authorization();
        var authorization = TestEntityFactory.createDefaultAuthorization();
        var client = TestEntityFactory.createDefaultRegisteredClient();
        given(authorizationRepository.findByRefreshTokenValue(anyString())).willReturn(Optional.of(authorization));
        given(clientService.findById(anyString())).willReturn(client);

        // WHEN
        var result = jpaOAuth2AuthorizationService.findByToken("token", OAuth2TokenType.REFRESH_TOKEN);

        // THEN
        assertThat(result).hasSameClassAs(auth2Authorization);
        then(authorizationRepository).should(atLeastOnce()).findByRefreshTokenValue(anyString());
    }

    @Test
    void findByToken_shouldReturnNull_whenThisTokenTypeNotExist() {
        // WHEN
        var result = jpaOAuth2AuthorizationService.findByToken("token", new OAuth2TokenType("token"));

        // THEN
        assertThat(result).isNull();
    }
}
