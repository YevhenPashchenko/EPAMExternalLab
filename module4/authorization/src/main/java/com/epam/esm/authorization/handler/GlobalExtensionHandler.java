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
public class GlobalExtensionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException exception) {
        var error = ErrorDto.builder().code(HttpStatus.NOT_FOUND.value()).message(exception.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleSQLException(DataIntegrityViolationException exception) {
        var message = exception.getMostSpecificCause().getLocalizedMessage();
        var error = ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(message.substring(message.indexOf("(")).replaceAll("[()]", ""))
            .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorDto> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        var error = ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        var error = ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
        var error = ErrorDto.builder()
            .code(HttpStatus.NOT_FOUND.value())
            .message(exception.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException exception) {
        var error = ErrorDto.builder()
            .code(HttpStatus.FORBIDDEN.value())
            .message(exception.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialException(BadCredentialsException exception) {
        var error = ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OAuth2AuthorizationCodeRequestAuthenticationException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationCodeRequestAuthenticationException(
        OAuth2AuthorizationCodeRequestAuthenticationException exception) {
        var error = ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        var error = ErrorDto.builder().code(HttpStatus.BAD_REQUEST.value()).message(exception.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
