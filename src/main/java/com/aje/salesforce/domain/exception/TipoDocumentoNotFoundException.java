package com.aje.salesforce.domain.exception;

public class TipoDocumentoNotFoundException extends RuntimeException {

    public TipoDocumentoNotFoundException(String id) {
        super("Tipo de documento not found with id: " + id);
    }
}
