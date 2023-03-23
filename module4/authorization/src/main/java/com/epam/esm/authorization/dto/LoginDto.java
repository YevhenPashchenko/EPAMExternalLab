package com.epam.esm.authorization.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class LoginDto {

    @NotBlank(message = "client id must be not empty")
    private String clientId;
    @Email(message = "email not valid")
    private String email;
    @NotBlank(message = "password must be not empty")
    private String password;
    @NotNull(message = "redirect uri must be not null")
    private String redirectUri;
    private Set<String> scopes;
}
