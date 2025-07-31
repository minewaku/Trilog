package com.minewaku.trilog.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.minewaku.trilog.dto.common.response.ErrorResponse;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(Exception.class);
    
    @Autowired
    private ErrorUtil errorUtil;

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<Object> handleException(ApiException e) {
        ErrorResponse errorDto = new ErrorResponse(e.getErrorCode(),
                                            e.getMessage(), 
                                            ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, e.getHttpStatus());
    }
    
    @ExceptionHandler(value = {BadCredentialsException.class})
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e) {
		ErrorResponse errorDto = new ErrorResponse(errorUtil.INVALID_CREDENTIALS,
											e.getMessage(), 
											ZonedDateTime.now(ZoneId.of("Z")));

		LOGGER.error(e.getMessage(), e);
		return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
	}


    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorDto = new ErrorResponse(errorUtil.INVALID_PARAMETERS,
                                        e.getMessage(), 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    //check later
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorResponse errorDto = new ErrorResponse(errorUtil.INTERNAL_SERVER_ERROR,
                                        e.getMessage(), 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }

    //check later
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        ErrorResponse errorDto = new ErrorResponse(errorUtil.CONSTRAINT_VIOLATION,
                                        e.getMessage(), 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = {AccessDeniedException.class})
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
		ErrorResponse errorDto = new ErrorResponse(errorUtil.ACCESS_DENIED, 
										MessageUtil.getMessage("access.denied"), 
										ZonedDateTime.now(ZoneId.of("Z")));

		LOGGER.error(e.getMessage(), e);
		return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
	}

    
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleOtherException(Exception e) {
        ErrorResponse errorDto = new ErrorResponse(errorUtil.INTERNAL_SERVER_ERROR,
                                        e.getMessage(), 
                                        ZonedDateTime.now(ZoneId.of("Z"))
        );

        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
