package com.bit4mation.web.exception;

import lombok.Data;

@Data
public class CustomError {
    private String message;
    private String exceptionClassName;
    private Long createDate;

    public CustomError() {
        createDate = System.currentTimeMillis();
    }

    public CustomError(String message) {
        this();
        this.message = message;
    }

    public CustomError(String message, Class<?> exceptionCls) {
        this(message);
        this.exceptionClassName = exceptionCls.getName();
    }
}
