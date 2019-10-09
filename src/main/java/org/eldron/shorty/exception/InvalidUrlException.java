package org.eldron.shorty.exception;

public class InvalidUrlException extends Exception {
    public InvalidUrlException(final String message) {
        super(message);
    }

    public InvalidUrlException(final Throwable cause) {
        super(cause);
    }
}
