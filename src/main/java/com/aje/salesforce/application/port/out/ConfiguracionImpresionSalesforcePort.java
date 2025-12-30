package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;

import java.util.List;

public interface ConfiguracionImpresionSalesforcePort {

    ConfiguracionImpresion findById(String id);

    List<ConfiguracionImpresion> findByPais(String pais);
}
