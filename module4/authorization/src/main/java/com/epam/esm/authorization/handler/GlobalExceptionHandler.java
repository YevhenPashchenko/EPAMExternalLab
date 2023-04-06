package com.epam.esm.authorization.handler;

import com.epam.esm.authorization.dto.ErrorDto;
import com.epam.esm.authorization.handler.exception.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException exception) {
        return getResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ResponseEntity<ErrorDto> getResponseEntity(HttpStatus httpStatus, String message) {
        var error = ErrorDto.builder().code(httpStatus.value()).message(message).build();
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleSQLException(DataIntegrityViolationException exception) {
        var message = exception.getMostSpecificCause().getLocalizedMessage();
        return getResponseEntity(HttpStatus.BAD_REQUEST,
            message.substring(message.indexOf("(")).replaceAll("[()]", ""));
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
        var error = ErrorDto.builder()
            .code(status.value())
            .message(message)
            .build();
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return getResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException exception) {
        return getResponseEntity(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialException(BadCredentialsException exception) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(OAuth2AuthorizationCodeRequestAuthenticationException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationCodeRequestAuthenticationException(
        OAuth2AuthorizationCodeRequestAuthenticationException exception) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> defaultExceptionHandler(Exception exception) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
