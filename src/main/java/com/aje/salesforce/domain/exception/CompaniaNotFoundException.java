package com.aje.salesforce.domain.exception;

public class CompaniaNotFoundException extends RuntimeException {
    
    public CompaniaNotFoundException(String id) {
        super(String.format("Compañía con ID '%s' no encontrada", id));
    }
    
    public CompaniaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
