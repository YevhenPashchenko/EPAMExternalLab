package com.epam.esm.authorization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "client")
public class Client {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;
    @Column(name = "client_id_issued_at", nullable = false)
    private Instant clientIdIssuedAt;
    @Column(name = "client_secret")
    private String clientSecret;
    @Column(name = "client_secret_expires_at")
    private Instant clientSecretExpiresAt;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "client_authentication_methods", nullable = false)
    private String clientAuthenticationMethods;
    @Column(name = "authorization_grant_types", nullable = false)
    private String authorizationGrantTypes;
    @Column(name = "redirect_uris")
    private String redirectUris;
    @Column(name = "scopes", nullable = false)
    private String scopes;
    @Column(name = "client_settings", nullable = false)
    private String clientSettings;
    @Column(name = "token_settings", nullable = false)
    private String tokenSettings;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Client that = (Client) obj;
        return Objects.equals(this.id, that.id) &&
            Objects.equals(this.clientId, that.clientId) &&
            Objects.equals(this.clientIdIssuedAt, that.clientIdIssuedAt) &&
            Objects.equals(this.clientSecret, that.clientSecret) &&
            Objects.equals(this.clientSecretExpiresAt, that.clientSecretExpiresAt) &&
            Objects.equals(this.clientName, that.clientName) &&
            Objects.equals(this.clientAuthenticationMethods, that.clientAuthenticationMethods) &&
            Objects.equals(this.authorizationGrantTypes, that.authorizationGrantTypes) &&
            Objects.equals(this.redirectUris, that.redirectUris) &&
            Objects.equals(this.scopes, that.scopes) &&
            Objects.equals(this.clientSettings, that.clientSettings) &&
            Objects.equals(this.tokenSettings, that.tokenSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.clientId, this.clientIdIssuedAt, this.clientSecret, this.clientSecretExpiresAt,
            this.clientName, this.clientAuthenticationMethods, this.authorizationGrantTypes, this.redirectUris,
            this.scopes, this.clientSettings, this.tokenSettings);
    }
}
