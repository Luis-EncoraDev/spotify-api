package com.spotify.api.config;

import com.spotify.api.OauthTokensException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionConfig extends ResponseEntityExceptionHandler {
    @ExceptionHandler({OauthTokensException.class})
    protected ResponseEntity<OauthTokensException> handleProductException(OauthTokensException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getStatus());
    }
}
