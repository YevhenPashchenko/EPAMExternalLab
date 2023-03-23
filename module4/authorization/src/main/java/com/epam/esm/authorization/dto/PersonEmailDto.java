package com.epam.esm.authorization.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonEmailDto {

    @Email(message = "email not valid")
    private String email;
}
