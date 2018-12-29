package com.example.demo.utils;

public class Base62Exception extends RuntimeException {
    public Base62Exception() {
    }

    public Base62Exception(String message) {
        super(message);
    }

    public Base62Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public Base62Exception(Throwable cause) {
        super(cause);
    }
}
