package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;

public interface GetConfiguracionImpresionByIdUseCase {

    ConfiguracionImpresion getById(String id);
}
