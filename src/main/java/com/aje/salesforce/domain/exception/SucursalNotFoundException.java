package com.aje.salesforce.domain.exception;

public class SucursalNotFoundException extends RuntimeException {

    public SucursalNotFoundException(String id) {
        super(String.format("Sucursal con ID '%s' no encontrada", id));
    }

    public SucursalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
