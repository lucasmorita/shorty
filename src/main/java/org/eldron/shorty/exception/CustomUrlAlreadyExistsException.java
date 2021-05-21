package org.eldron.shorty.exception;

public class CustomUrlAlreadyExistsException extends RuntimeException {
    public CustomUrlAlreadyExistsException(final String message) {
        super(message);
    }
}
