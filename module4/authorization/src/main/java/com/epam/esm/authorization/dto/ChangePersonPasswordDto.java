package com.epam.esm.authorization.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class ChangePersonPasswordDto {

    @NotBlank(message = "Old password can't be empty")
    private String oldPassword;
    @NotBlank(message = "New password can't be empty")
    private String newPassword;
}
