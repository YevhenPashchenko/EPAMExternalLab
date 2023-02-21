package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7591436825266771104L;
    private final int httpErrorCode;

    public EntityNotFoundException(String errorMessage, int httpErrorCode) {
        super(errorMessage);
        this.httpErrorCode = httpErrorCode;
    }
}
