package com.spotify.api;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    public final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
