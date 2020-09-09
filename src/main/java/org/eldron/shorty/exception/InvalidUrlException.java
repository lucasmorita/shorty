package org.eldron.shorty.exception;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(final String url) {
        super("Invalid url, url=" + url);
    }

    public InvalidUrlException(final Throwable cause) {
        super(cause);
    }
}
