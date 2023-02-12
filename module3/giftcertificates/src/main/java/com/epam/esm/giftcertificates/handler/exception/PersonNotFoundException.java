package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class PersonNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -3100153359520511054L;

  public PersonNotFoundException(Long id) {
    super("Requested resource not found (id = " + id + ")");
  }

  public PersonNotFoundException(String message) {
    super(message);
  }
}
