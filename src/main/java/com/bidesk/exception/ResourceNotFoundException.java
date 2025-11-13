package com.bidesk.exception;

/**
 * Exceção customizada para recursos não encontrados.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}


