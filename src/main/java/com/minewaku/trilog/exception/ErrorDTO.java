package com.minewaku.trilog.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class ErrorDTO {
    private final String title;
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public ErrorDTO(String title, String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.title = title;
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}


