package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.TipoDocumento;

public interface GetTipoDocumentoByIdUseCase {

    TipoDocumento getById(String id);
}
