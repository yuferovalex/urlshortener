package com.example.demo.service;

public class LinkNotFoundException extends RuntimeException {
    LinkNotFoundException(String message) {
        super(message);
    }
}
