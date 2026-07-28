package io.deccan.controlplane.common.exception;

import io.deccan.controlplane.common.response.ErrorResponse;
import io.deccan.controlplane.identity.exception.IdentityAlreadyExistsException;
import io.deccan.controlplane.identity.exception.IdentityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdentityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            IdentityNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.builder()
                                .status(404)
                                .error("NOT_FOUND")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
    }

    @ExceptionHandler(IdentityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(
            IdentityAlreadyExistsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .status(409)
                                .error("ALREADY_EXISTS")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest()
                .body(
                        ErrorResponse.builder()
                                .status(400)
                                .error("VALIDATION_FAILED")
                                .message("Validation failed")
                                .path(request.getRequestURI())
                                .validationErrors(errors)
                                .build()
                );
    }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(
                Exception ex,
                HttpServletRequest request) {

         log.error(
                "Unhandled exception while processing {} {}",
                request.getMethod(),
                request.getRequestURI(),
                ex);

        return ResponseEntity.internalServerError()
                .body(
                        ErrorResponse.builder()
                                .status(500)
                                .error("INTERNAL_SERVER_ERROR")
                                .message(ex.getClass().getSimpleName() + ": " + ex.getMessage())
                                .path(request.getRequestURI())
                                .build()
                );
        }

}