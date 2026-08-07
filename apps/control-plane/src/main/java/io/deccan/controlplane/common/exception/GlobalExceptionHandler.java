package io.deccan.controlplane.common.exception;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.common.response.ErrorResponse;
import io.deccan.controlplane.connector.exception.ConnectorNotFoundException;
import io.deccan.controlplane.identity.exception.IdentityAlreadyExistsException;
import io.deccan.controlplane.identity.exception.IdentityNotFoundException;
import io.deccan.controlplane.worker.exception.WorkerNotFoundException;
import io.deccan.controlplane.workflow.exception.WorkflowNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

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

    @ExceptionHandler({
            AuthorizationDeniedException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            RuntimeException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ErrorResponse.builder()
                                .status(403)
                                .error("ACCESS_DENIED")
                                .message("You do not have permission to perform this action")
                                .path(request.getRequestURI())
                                .build()
                );
    }
        @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(
                Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .status(401).error("INVALID_CREDENTIALS")
                        .message("Invalid email or password")
                        .path(request.getRequestURI()).build());
        }

        @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleMalformedJson(
                Exception ex, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .status(400).error("MALFORMED_REQUEST")
                        .message("Request body is malformed or contains an invalid value")
                        .path(request.getRequestURI()).build());
        }

        @ExceptionHandler({
                io.deccan.controlplane.workflow.definition.validation.WorkflowValidationException.class,
                io.deccan.controlplane.workflow.lifecycle.WorkflowLifecycleException.class,
                IllegalArgumentException.class
        })
        public ResponseEntity<ErrorResponse> handleBadRequest(
                RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .status(400).error("BAD_REQUEST")
                        .message(ex.getMessage())
                        .path(request.getRequestURI()).build());
        }
        @ExceptionHandler({
        WorkflowNotFoundException.class,
        ConnectorNotFoundException.class,
        WorkerNotFoundException.class
                })
                public ResponseEntity<ApiResponse<Void>> handleNotFound(
                        RuntimeException ex) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                ApiResponse.<Void>builder()
                                        .status(404)
                                        .message(ex.getMessage())
                                        .build());
                }
        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ErrorResponse> handleConflict(
                IllegalStateException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .status(409).error("CONFLICT")
                        .message(ex.getMessage())
                        .path(request.getRequestURI()).build());
        }



@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ErrorResponse> handleEntityNotFound(
        EntityNotFoundException ex,
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

@ExceptionHandler(ConstraintViolationException.class)
public ResponseEntity<ErrorResponse> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest request) {

    return ResponseEntity.badRequest()
            .body(
                    ErrorResponse.builder()
                            .status(400)
                            .error("VALIDATION_FAILED")
                            .message(ex.getMessage())
                            .path(request.getRequestURI())
                            .build()
            );
}

@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
        DataIntegrityViolationException ex,
        HttpServletRequest request) {

    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                    ErrorResponse.builder()
                            .status(409)
                            .error("DATA_INTEGRITY_VIOLATION")
                            .message("Database constraint violated")
                            .path(request.getRequestURI())
                            .build()
            );
}

@ExceptionHandler(MissingServletRequestParameterException.class)
public ResponseEntity<ErrorResponse> handleMissingParameter(
        MissingServletRequestParameterException ex,
        HttpServletRequest request) {

    return ResponseEntity.badRequest()
            .body(
                    ErrorResponse.builder()
                            .status(400)
                            .error("MISSING_PARAMETER")
                            .message(ex.getParameterName() + " is required")
                            .path(request.getRequestURI())
                            .build()
            );
}

@ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<ErrorResponse> handleTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request) {

    return ResponseEntity.badRequest()
            .body(
                    ErrorResponse.builder()
                            .status(400)
                            .error("INVALID_PARAMETER")
                            .message("Invalid value for parameter '" + ex.getName() + "'")
                            .path(request.getRequestURI())
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