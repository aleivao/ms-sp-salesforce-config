package com.aje.salesforce.domain.exception;

public class ConfiguracionImpresionNotFoundException extends RuntimeException {

    public ConfiguracionImpresionNotFoundException(String id) {
        super("Configuracion de impresion not found with id: " + id);
    }
}
