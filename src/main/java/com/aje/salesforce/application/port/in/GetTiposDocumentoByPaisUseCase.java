package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.TipoDocumento;

import java.util.List;

public interface GetTiposDocumentoByPaisUseCase {

    List<TipoDocumento> getByPais(String pais);
}
