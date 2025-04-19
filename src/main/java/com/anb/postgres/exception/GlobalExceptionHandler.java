package com.anb.postgres.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex){
        return buildErrResponse(ex,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Map<String,Object>> handleInternal (InternalServerException ex){
        return buildErrResponse(ex,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteOperationException.class)
     public ResponseEntity<Map<String, Object>> handleDelete(DeleteOperationException ex){
        return buildErrResponse(ex,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(DataIntegrityViolationException ex) {
        return buildErrResponse(new RuntimeException("Duplicate record not allowed."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceFoundException(ResourceNotFoundException ex){
        return  buildErrResponse(ex,HttpStatus.NOT_FOUND);
    }
    private ResponseEntity<Map<String, Object>> buildErrResponse(RuntimeException ex , HttpStatus status) {

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message" , ex.getMessage());
        error.put("status" , status.value());
        return new ResponseEntity<>(error,status);
    }
}

