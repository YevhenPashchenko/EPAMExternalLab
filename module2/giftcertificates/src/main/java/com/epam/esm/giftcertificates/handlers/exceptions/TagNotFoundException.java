package com.epam.esm.giftcertificates.handlers.exceptions;

import java.io.Serial;

public class TagNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4024507632300967206L;

    public TagNotFoundException(long id) {
        super("Requested resource not found (id = " + id + ")");
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
