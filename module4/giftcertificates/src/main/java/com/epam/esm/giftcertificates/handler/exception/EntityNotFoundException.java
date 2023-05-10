package com.epam.esm.giftcertificates.handler.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5503910493871734756L;
    private final int httpErrorCode;

    public EntityNotFoundException(String errorMessage, int httpErrorCode) {
        super(errorMessage);
        this.httpErrorCode = httpErrorCode;
    }
}
