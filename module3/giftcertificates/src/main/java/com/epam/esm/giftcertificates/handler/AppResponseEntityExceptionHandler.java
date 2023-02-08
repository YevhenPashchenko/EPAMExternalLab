package com.epam.esm.giftcertificates.handler;

import com.epam.esm.giftcertificates.constant.ErrorCodeConstant;
import com.epam.esm.giftcertificates.dto.ErrorDto;
import com.epam.esm.giftcertificates.handler.exception.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.OrderNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.OrderTotalCostCalculationException;
import com.epam.esm.giftcertificates.handler.exception.TagNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(TagNotFoundException.class)
  public ResponseEntity<ErrorDto> handleTagNotFoundException(Exception exception) {
    var error =
        ErrorDto.builder()
            .code(ErrorCodeConstant.TAG_NOT_FOUND)
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(GiftCertificateNotFoundException.class)
  public ResponseEntity<ErrorDto> handleGiftCertificateNotFoundException(Exception exception) {
    var error =
        ErrorDto.builder()
            .code(ErrorCodeConstant.GIFT_CERTIFICATE_NOT_FOUND)
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDto> handleUserNotFoundException(Exception exception) {
    var error =
        ErrorDto.builder()
            .code(ErrorCodeConstant.USER_NOT_FOUND)
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorDto> handleOrderNotFoundException(Exception exception) {
    var error =
        ErrorDto.builder()
            .code(ErrorCodeConstant.ORDER_NOT_FOUND)
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(OrderTotalCostCalculationException.class)
  public ResponseEntity<ErrorDto> handleOrderTotalCostException(Exception exception) {
    var error =
        ErrorDto.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<ErrorDto> handleEmptyResultDataAccessException(
      EmptyResultDataAccessException exception) {
    var error =
        ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolationException(
      ConstraintViolationException exception) {
    var error =
        ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      @NonNull HttpHeaders headers,
      HttpStatusCode status,
      @NonNull WebRequest request) {
    var fieldError = exception.getBindingResult().getFieldError();
    var message = fieldError != null ? fieldError.getDefaultMessage() : exception.getMessage();
    var error = ErrorDto.builder().code(status.value()).message(message).build();
    return new ResponseEntity<>(error, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> defaultExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        ErrorDto.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message(exception.getMessage())
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
