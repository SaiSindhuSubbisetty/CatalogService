package org.example.catalogservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.catalogservice.constants.Constants.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RestaurantAlreadyExistsException.class)
    public ResponseEntity<String> handleRestaurantAlreadyExistsException() {
        return ResponseEntity.badRequest().body(RESTAURANT_ALREADY_EXISTS);
    }

    @ExceptionHandler(value = RestaurantNotFoundException.class)
    public ResponseEntity<String> handleRestaurantNotFoundException() {
        return ResponseEntity.badRequest().body(RESTAURANT_NOT_FOUND);
    }

    @ExceptionHandler(value = ItemAlreadyExistsException.class)
    public ResponseEntity<String> handleItemAlreadyExistsException() {
        return ResponseEntity.badRequest().body(ITEM_ALREADY_EXISTS);
    }

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFoundException() {
        return ResponseEntity.badRequest().body(ITEM_NOT_FOUND);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(e.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
    }
}
