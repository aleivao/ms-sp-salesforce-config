package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.Sucursal;

public interface GetSucursalByIdUseCase {

    Sucursal getById(String id);
}
