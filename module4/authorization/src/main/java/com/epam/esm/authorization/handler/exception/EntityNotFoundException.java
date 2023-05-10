package com.epam.esm.authorization.handler.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7591436825266771104L;

    public EntityNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
