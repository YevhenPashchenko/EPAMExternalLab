package com.epam.esm.giftcertificates.handler;

import com.epam.esm.giftcertificates.dto.ErrorDto;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.ReceiptTotalCostCalculationException;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException exception) {
        var error = ErrorDto.builder().code(exception.getHttpErrorCode()).message(exception.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReceiptTotalCostCalculationException.class)
    public ResponseEntity<ErrorDto> handleRecipeTotalCostException(Exception exception) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<ErrorDto> getResponseEntity(HttpStatus httpStatus, String message) {
        var error = ErrorDto.builder().code(httpStatus.value()).message(message).build();
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorDto> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
        @NonNull HttpHeaders headers, HttpStatusCode status, @NonNull WebRequest request) {
        var fieldError = exception.getBindingResult().getFieldError();
        var message = fieldError != null ? fieldError.getDefaultMessage() : exception.getMessage();
        var error = ErrorDto.builder().code(status.value()).message(message).build();
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleSQLException(DataIntegrityViolationException exception) {
        var message = exception.getMostSpecificCause().getLocalizedMessage();
        return getResponseEntity(HttpStatus.BAD_REQUEST,
            message.substring(message.indexOf("(")).replaceAll("[()]", ""));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException exception) {
        return getResponseEntity(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> defaultExceptionHandler(Exception exception) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
