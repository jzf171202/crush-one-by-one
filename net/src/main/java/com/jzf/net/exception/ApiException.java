package com.jzf.net.exception;

public class ApiException extends Exception {
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
