package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.DetallesDocumento;

import java.util.List;

public interface DetallesDocumentoSalesforcePort {
    DetallesDocumento findById(String id);
    List<DetallesDocumento> findByConfiguracionImpresion(String configuracionImpresionId);
}
