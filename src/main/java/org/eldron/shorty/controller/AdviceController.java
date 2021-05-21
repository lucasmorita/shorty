package org.eldron.shorty.controller;

import lombok.extern.slf4j.Slf4j;
import org.eldron.shorty.exception.CustomUrlAlreadyExistsException;
import org.eldron.shorty.exception.InvalidUrlException;
import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.vo.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Void> handleUrlNotFoundException(final UrlNotFoundException exception) {
        log.error("m=handleUrlNotFoundException", exception);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUrlException(final InvalidUrlException exception) {
        log.error("m=handleInvalidUrlException", exception);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Invalid url"));
    }

    @ExceptionHandler(CustomUrlAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomUrlAlreadyExists(final CustomUrlAlreadyExistsException exception) {
        log.error("m=handleCustomUrlAlreadyExists", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(String.format("Custom url '%s' already exists", exception.getMessage())));
    }

}
