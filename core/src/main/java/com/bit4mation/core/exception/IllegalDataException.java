package com.bit4mation.core.exception;

public class IllegalDataException extends RuntimeException {

    public static final String ROOT_NODE_DELETE = "You cannot delete root node";
    public static final String CHANGE_ROOT_NOTE = "Cannot change root node value";

    public IllegalDataException(String val) {
        super(val);
    }
}
