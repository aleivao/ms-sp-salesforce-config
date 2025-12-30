package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.TipoDocumento;

import java.util.List;

public interface TipoDocumentoSalesforcePort {

    TipoDocumento findById(String id);

    List<TipoDocumento> findByPais(String pais);
}
