package br.com.gramado.parkingapp.util.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionHandlerUtil {

    private final StandardError err = new StandardError();

    private void setExceptionData(Integer status, String errorDescription, String path, String message) {
        err.setTimestamp(Instant.now());
        err.setStatus(status);
        err.setError(errorDescription);
        err.setMessage(message);
        err.setPath(path);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardError> entityNotFound(NotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        setExceptionData(status.value(), "Entity Not Found", request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(status).body(this.err);
    }

    @ExceptionHandler(ValidationsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> validationsError(ValidationsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        setExceptionData(status.value(), "", request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(status).body(this.err);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardError> genericError(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        setExceptionData(status.value(), "Unknown Error", request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(status).body(this.err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> dtoValidations(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        setExceptionData(status.value(),
                "Data Error",
                request.getRequestURI(),
                e.getFieldError() == null ?
                        e.getMessage() :
                        e.getFieldError().getField() + ": " + e.getFieldError().getDefaultMessage()
        );

        return ResponseEntity.status(status).body(this.err);
    }
}
