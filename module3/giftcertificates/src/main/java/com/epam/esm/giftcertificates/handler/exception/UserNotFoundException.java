package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = -3100153359520511054L;

  public UserNotFoundException(Long id) {
    super("Requested resource not found (id = " + id + ")");
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
