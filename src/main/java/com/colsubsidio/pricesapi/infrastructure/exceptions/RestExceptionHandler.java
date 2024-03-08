package com.colsubsidio.pricesapi.infrastructure.exceptions;

import com.colsubsidio.pricesapi.domain.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    protected ResponseEntity<ExceptionResponse> handleException(DuplicateKeyException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(httpStatus, ex);
    }

    @ExceptionHandler
    protected ResponseEntity<ExceptionResponse> handleException(ConstraintViolationException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Set<String> errors = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        String message = String.join(" + ", errors);
        return buildResponseEntity(httpStatus, new RuntimeException(message));
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(HttpStatus httpStatus, Exception ex) {
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, httpStatus);
    }
}
