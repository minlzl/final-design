package com.example.finalend.exception;

public class ApiException extends RuntimeException {

    public ApiException() {
        this("fail");
    }

    public ApiException(String message) {
        super(message);
    }
}
