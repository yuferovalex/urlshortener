package edu.yuferovalex.urlshortener.service;

public class UrlServiceException extends RuntimeException {
    public UrlServiceException(String message) {
        super(message);
    }

    public UrlServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
