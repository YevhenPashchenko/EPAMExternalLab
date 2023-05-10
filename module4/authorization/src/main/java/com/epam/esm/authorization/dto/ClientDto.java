package com.epam.esm.authorization.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class ClientDto extends RepresentationModel<ClientDto> {

    @NotBlank(message = "Client id can't be empty")
    private String clientId;
    @NotBlank(message = "Client secret can't be empty")
    private String clientSecret;
    @NotNull(message = "Scopes must not be null")
    private Set<String> scopes = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ClientDto clientDto = (ClientDto) o;
        return clientId.equals(clientDto.clientId) && clientSecret.equals(clientDto.clientSecret) && scopes.equals(
            clientDto.scopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientId, clientSecret, scopes);
    }
}
