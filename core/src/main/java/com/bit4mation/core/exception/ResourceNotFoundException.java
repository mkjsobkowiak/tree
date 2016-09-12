package com.bit4mation.core.exception;

public class ResourceNotFoundException extends RuntimeException {

    public static final String ENTITY_NOT_FOUND = "Entity not found (id=%s)";

    public ResourceNotFoundException(String val) {
        super(val);
    }
}
