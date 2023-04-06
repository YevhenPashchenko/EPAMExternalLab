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
@Entity(name = "`authorization`")
public class Authorization {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Column(name = "registered_client_id", nullable = false)
    private String registeredClientId;
    @Column(name = "principal_name", nullable = false)
    private String principalName;
    @Column(name = "authorization_grant_type", nullable = false)
    private String authorizationGrantType;
    @Column(name = "authorized_scopes")
    private String authorizedScopes;
    @Column(name = "attributes")
    private String attributes;
    @Column(name = "state")
    private String state;

    @Column(name = "authorization_code_value")
    private String authorizationCodeValue;
    @Column(name = "authorization_code_issued_at")
    private Instant authorizationCodeIssuedAt;
    @Column(name = "authorization_code_expires_at")
    private Instant authorizationCodeExpiresAt;
    @Column(name = "authorization_code_metadata")
    private String authorizationCodeMetadata;

    @Column(name = "access_token_value")
    private String accessTokenValue;
    @Column(name = "access_token_issued_at")
    private Instant accessTokenIssuedAt;
    @Column(name = "access_token_expires_at")
    private Instant accessTokenExpiresAt;
    @Column(name = "access_token_metadata")
    private String accessTokenMetadata;
    @Column(name = "access_token_type")
    private String accessTokenType;
    @Column(name = "access_token_scopes")
    private String accessTokenScopes;

    @Column(name = "refresh_token_value")
    private String refreshTokenValue;
    @Column(name = "refresh_token_issued_at")
    private Instant refreshTokenIssuedAt;
    @Column(name = "refresh_token_expires_at")
    private Instant refreshTokenExpiresAt;
    @Column(name = "refresh_token_metadata")
    private String refreshTokenMetadata;

    @Column(name = "oidc_id_token_value")
    private String oidcIdTokenValue;
    @Column(name = "oidc_id_token_issued_at")
    private Instant oidcIdTokenIssuedAt;
    @Column(name = "oidc_id_token_expires_at")
    private Instant oidcIdTokenExpiresAt;
    @Column(name = "oidc_id_token_metadata")
    private String oidcIdTokenMetadata;
    @Column(name = "oidc_id_token_claims")
    private String oidcIdTokenClaims;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Authorization that = (Authorization) o;
        return Objects.equals(id, that.id) && Objects.equals(registeredClientId,
            that.registeredClientId) && Objects.equals(principalName, that.principalName)
            && Objects.equals(authorizationGrantType, that.authorizationGrantType) && Objects.equals(
            authorizedScopes, that.authorizedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registeredClientId, principalName, authorizationGrantType, authorizedScopes);
    }
}
