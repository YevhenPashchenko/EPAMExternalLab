package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class TagNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -4431132767733561025L;

  public TagNotFoundException(Long id) {
    super("Requested resource not found (id = " + id + ")");
  }

  public TagNotFoundException(String message) {
    super(message);
  }
}
