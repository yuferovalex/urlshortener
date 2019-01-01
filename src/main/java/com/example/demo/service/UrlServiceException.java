package com.example.demo.service;

public class UrlServiceException extends RuntimeException {
    UrlServiceException(String message) {
        super(message);
    }

    UrlServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
