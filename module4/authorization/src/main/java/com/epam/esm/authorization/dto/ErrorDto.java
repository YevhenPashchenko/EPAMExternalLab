package com.epam.esm.authorization.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDto {

    private int code;
    private String message;
}
