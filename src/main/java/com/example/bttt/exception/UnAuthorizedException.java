package com.example.bttt.exception;

public class UnAuthorizedException extends CustomException {
    public UnAuthorizedException(String message) {
        super(401, message);
    }
}
