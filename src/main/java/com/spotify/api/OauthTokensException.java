package com.spotify.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OauthTokensException extends RuntimeException {

    private final HttpStatus status;
    public OauthTokensException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
