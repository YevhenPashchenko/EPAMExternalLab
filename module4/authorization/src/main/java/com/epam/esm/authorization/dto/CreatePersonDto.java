package com.epam.esm.authorization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePersonDto {

    @Email(message = "email not valid")
    private String email;

    @NotBlank(message = "password must be not empty")
    private String password;
}
