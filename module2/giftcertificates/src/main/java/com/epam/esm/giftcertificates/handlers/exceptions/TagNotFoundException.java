package com.epam.esm.giftcertificates.handlers.exceptions;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(long id) {
        super("Requested resource not found (id = " + id + ")");
    }
}
