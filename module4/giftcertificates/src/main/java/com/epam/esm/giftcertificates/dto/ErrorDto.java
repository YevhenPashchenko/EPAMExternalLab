package com.epam.esm.giftcertificates.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorDto {

    private int code;
    private String message;
}
