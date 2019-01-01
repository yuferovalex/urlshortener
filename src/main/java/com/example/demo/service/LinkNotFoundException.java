package com.example.demo.service;

public class LinkNotFoundException extends UrlServiceException {
    LinkNotFoundException(String link) {
        super(String.format("link '%s' not found", link));
    }
}
