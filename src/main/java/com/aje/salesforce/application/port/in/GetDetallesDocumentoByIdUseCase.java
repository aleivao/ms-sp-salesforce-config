package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.DetallesDocumento;

public interface GetDetallesDocumentoByIdUseCase {
    DetallesDocumento getById(String id);
}
