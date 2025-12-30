package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.DetallesDocumento;

import java.util.List;

public interface GetDetallesDocumentoByConfiguracionUseCase {
    List<DetallesDocumento> getByConfiguracion(String configuracionImpresionId);
}
