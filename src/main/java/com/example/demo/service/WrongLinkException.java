package com.example.demo.service;

public class WrongLinkException extends UrlServiceException {
    WrongLinkException(String link, Throwable cause) {
        super(String.format("wrong link format given '%s'", link), cause);
    }
}
