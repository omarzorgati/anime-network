package com.omar.anime_network.exceptionHandler;

import com.omar.anime_network.exceptionHandler.customExceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle authentication errors (e.g., invalid credentials)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException exp) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                                .businessExceptionDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                                .error(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                                .build()
                );
    }

    // Handle validation errors (e.g., @Valid failed)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationErrors(MethodArgumentNotValidException exp) {
        // Collect only the error messages, excluding field names
        var errors = exp.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage()) // Extract only the default message
                .collect(Collectors.joining(", "));     // Join messages with a comma

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(errors)
                                .build()
                );
    }

    // Handle custom business exceptions
    @ExceptionHandler({UsernameNotFoundException.class, CategoryNotFoundException.class,
            AnimeNotFoundException.class,ReviewNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundBusinessExceptions(RuntimeException exp) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }
    @ExceptionHandler({PasswordsDoNotMatchException.class, CategoryAlreadyExistsException.class,
            OtpExpiredException.class, InvalidOtpException.class,FileStorageException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestBusinessExceptions(RuntimeException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }
}

