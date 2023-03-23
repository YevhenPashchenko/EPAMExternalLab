package com.epam.esm.authorization.util;

import com.epam.esm.authorization.dto.ChangePersonPasswordDto;
import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.dto.LoginDto;
import com.epam.esm.authorization.dto.PersonDto;
import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.entity.Authorization;
import com.epam.esm.authorization.entity.Client;
import com.epam.esm.authorization.entity.Person;
import com.epam.esm.authorization.entity.PersonAuthority;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.time.Instant;
import java.util.Set;

@UtilityClass
public class TestEntityFactory {

    private static final String EMAIL = "user@mail.com";
    private static final String PASSWORD = "password";
    private static final String NEW_PASSWORD = "pass";
    private static final Boolean ENABLED = true;
    private static final String AUTHORITY_VALUE = "user";
    private static final String UPDATE_AUTHORITY_VALUE = "ROLE_admin";
    private static final String CLIENT_ID = "id";
    private static final String CLIENT_CLIENT_ID = "client";
    private static final String UPDATE_CLIENT_ID = "update-client";
    private static final String CLIENT_SECRET = "client-secret";
    private static final String UPDATE_CLIENT_SECRET = "update-client-secret";
    private static final String CLIENT_GRANT_TYPES = "refresh_token,authorization_code";
    private static final String CLIENT_SCOPES = "clients.read";
    private static final String UPDATE_CLIENT_SCOPES = "clients.write";
    private static final String CLIENT_SETTINGS =
        "{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false}";
    private static final String TOKEN_SETTINGS =
        "{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true}";
    private static final String REDIRECT_URI = "http://127.0.0.1:9000";
    private static final String PRINCIPAL_NAME = "principalName";
    private static final String ATTRIBUTE = "{\"@class\":\"java.util.Collections$UnmodifiableMap\"}";

    public LoginDto createDefaultLoginDto() {
        var login = new LoginDto();
        login.setClientId(CLIENT_CLIENT_ID);
        login.setEmail(EMAIL);
        login.setPassword(PASSWORD);
        login.setRedirectUri(REDIRECT_URI);
        login.setScopes(Set.of(CLIENT_SCOPES));
        return login;
    }

    public OAuth2Authorization createDefaultOAuth2Authorization() {
        return OAuth2Authorization.withRegisteredClient(createDefaultRegisteredClient())
            .principalName(PRINCIPAL_NAME)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .build();
    }

    public RegisteredClient createDefaultRegisteredClient() {
        return RegisteredClient.withId(CLIENT_ID)
            .clientId(CLIENT_CLIENT_ID)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(REDIRECT_URI)
            .scope(CLIENT_SCOPES)
            .build();
    }

    public Authorization createDefaultAuthorization() {
        var authorization = new Authorization();
        authorization.setRegisteredClientId(CLIENT_ID);
        authorization.setPrincipalName(PRINCIPAL_NAME);
        authorization.setAuthorizationGrantType("authorization_code");
        authorization.setAttributes(ATTRIBUTE);
        return authorization;
    }

    public Client createDefaultClient() {
        var client = new Client();
        client.setId(CLIENT_ID);
        client.setClientId(CLIENT_CLIENT_ID);
        client.setClientIdIssuedAt(Instant.now());
        client.setClientSecret(CLIENT_SECRET);
        client.setClientAuthenticationMethods("authorization_code");
        client.setAuthorizationGrantTypes(CLIENT_GRANT_TYPES);
        client.setRedirectUris(REDIRECT_URI);
        client.setScopes(CLIENT_SCOPES);
        client.setClientSettings(CLIENT_SETTINGS);
        client.setTokenSettings(TOKEN_SETTINGS);
        return client;
    }

    public ClientDto createDefaultClientDto() {
        var client = new ClientDto();
        client.setClientId(CLIENT_CLIENT_ID);
        client.setClientSecret(CLIENT_SECRET);
        client.setScopes(Set.of(CLIENT_SCOPES));
        return client;
    }

    public UpdateClientDto createDefaultUpdateClientDto() {
        var client = new UpdateClientDto();
        client.setClientId(UPDATE_CLIENT_ID);
        client.setClientSecret(UPDATE_CLIENT_SECRET);
        client.setScopes(Set.of(UPDATE_CLIENT_SCOPES));
        return client;
    }

    public Person createDefaultPerson() {
        var person = new Person();
        person.setEmail(EMAIL);
        person.setPassword(PASSWORD);
        person.setEnabled(ENABLED);

        var personAuthority = new PersonAuthority();
        personAuthority.setAuthority(AUTHORITY_VALUE);
        person.addPersonAuthority(personAuthority);

        return person;
    }

    public PersonDto createUpdatePerson() {
        var person = new PersonDto();
        person.setEmail(EMAIL);
        person.setEnabled(!ENABLED);
        person.setAuthorities(Set.of(UPDATE_AUTHORITY_VALUE));
        return person;
    }

    public UserDetails createDefaultUser() {
        return User.withUsername(EMAIL)
            .password(PASSWORD)
            .disabled(!ENABLED)
            .authorities(AUTHORITY_VALUE)
            .build();
    }

    public PersonDto createDefaultPersonDto() {
        var person = new PersonDto();
        person.setEmail(EMAIL);
        person.setEnabled(ENABLED);
        person.setAuthorities(Set.of(AUTHORITY_VALUE));
        return person;
    }

    public ChangePersonPasswordDto createDefaultChangePersonPassword() {
        var personPassword = new ChangePersonPasswordDto();
        personPassword.setOldPassword(PASSWORD);
        personPassword.setNewPassword(NEW_PASSWORD);
        return personPassword;
    }
}
