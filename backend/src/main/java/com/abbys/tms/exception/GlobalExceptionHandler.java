package com.abbys.tms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ApiError> handleJwtValidation(JwtValidationException ex, WebRequest req){
        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                req.getDescription(false).replace("uri=","")
        );

        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ApiError> handleContentNotFound(NotFound ex, WebRequest req){
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                req.getDescription(false).replace("uri=","")
        );

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiError> handleDatabaseConflicts(DatabaseException ex, WebRequest req){
        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                req.getDescription(false).replace("uri=","")
        );

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentException(MethodArgumentNotValidException ex, WebRequest req){
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                req.getDescription(false).replace("uri=","")
        );

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest req) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                ex.getMessage(),
                req.getDescription(false).replace("uri=","")
        );
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
