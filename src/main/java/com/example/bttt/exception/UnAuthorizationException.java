package com.example.bttt.exception;

public class UnAuthorizationException extends CustomException {
    public UnAuthorizationException(String message) {
        super(403, message);
    }
}
