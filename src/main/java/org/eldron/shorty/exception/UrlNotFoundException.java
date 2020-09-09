package org.eldron.shorty.exception;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException() {
        super("Url was not found");
    }
}
