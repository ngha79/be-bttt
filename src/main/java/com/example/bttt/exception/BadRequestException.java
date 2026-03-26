package com.example.bttt.exception;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(400, message);
    }
}
