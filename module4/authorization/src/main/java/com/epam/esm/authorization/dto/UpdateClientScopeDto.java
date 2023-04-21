package com.epam.esm.authorization.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class UpdateClientScopeDto {

    @NotBlank(message = "client scope must be not empty")
    private String oldScope;
    @NotBlank(message = "new client scope must be not empty")
    private String newScope;
}
