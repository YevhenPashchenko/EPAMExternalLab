package com.epam.esm.authorization.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class UpdatePersonRoleDto {

    @NotBlank(message = "person role must be not empty")
    private String oldRole;
    @NotBlank(message = "new person role must be not empty")
    private String newRole;
}
