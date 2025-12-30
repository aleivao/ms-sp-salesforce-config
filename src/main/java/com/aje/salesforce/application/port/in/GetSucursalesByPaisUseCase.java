package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.Sucursal;

import java.util.List;

public interface GetSucursalesByPaisUseCase {

    List<Sucursal> getByPais(String pais);
}
