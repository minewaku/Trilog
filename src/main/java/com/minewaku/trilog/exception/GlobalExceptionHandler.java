package com.minewaku.trilog.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(Exception.class);
    
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(EntityNotFoundException e) {
        ErrorDTO errorDto = new ErrorDTO("Internal Error",
                                            e.getMessage(), 
                                            HttpStatus.BAD_REQUEST, 
                                            ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorDTO errorDto = new ErrorDTO("Type mismatch",
                                        e.getMessage(), 
                                        HttpStatus.BAD_REQUEST, 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorDTO errorDto = new ErrorDTO("Entity not found",
                                        e.getMessage(), 
                                        HttpStatus.NOT_FOUND, 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        ErrorDTO errorDto = new ErrorDTO("Invalid request parameters",
                                        e.getMessage(), 
                                        HttpStatus.BAD_REQUEST, 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleOtherException(Exception e) {
        ErrorDTO errorDto = new ErrorDTO("",
                                        e.getMessage(), 
                                        HttpStatus.BAD_REQUEST, 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
