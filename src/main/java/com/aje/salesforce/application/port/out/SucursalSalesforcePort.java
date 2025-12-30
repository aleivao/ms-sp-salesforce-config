package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.Sucursal;

import java.util.List;

public interface SucursalSalesforcePort {

    Sucursal findById(String id);

    List<Sucursal> findByPais(String pais);
}
