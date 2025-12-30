package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;

import java.util.List;

public interface GetConfiguracionesImpresionByPaisUseCase {

    List<ConfiguracionImpresion> getByPais(String pais);
}
