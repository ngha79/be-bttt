package com.example.bttt.exception;

public class CustomException extends RuntimeException {
    private int code;

    public CustomException(String message) {
        super(message);
        this.code = 500;
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
