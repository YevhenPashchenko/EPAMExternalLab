package com.epam.esm.giftcertificates.handler.exception;

import java.io.Serial;

public class RecipeNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -7372869782636206505L;

  public RecipeNotFoundException(Long id) {
    super("Requested resource not found (id = " + id + ")");
  }

  public RecipeNotFoundException(String message) {
    super(message);
  }
}
