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
        var error = ErrorDto.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(exception.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorDto> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        var error = ErrorDto.builder().code(HttpStatus.BAD_REQUEST.value()).message(exception.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        var error = ErrorDto.builder().code(HttpStatus.BAD_REQUEST.value()).message(exception.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
        var error =
            ErrorDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Could not create new tag. Tag with name" + message.substring(message.indexOf("=")))
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        var error = ErrorDto.builder().code(HttpStatus.BAD_REQUEST.value()).message(exception.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> defaultExceptionHandler(Exception exception) {
        return new ResponseEntity<>(
            ErrorDto.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(exception.getMessage()).build(),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
