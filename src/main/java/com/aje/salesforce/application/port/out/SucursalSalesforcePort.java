package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.Sucursal;

public interface SucursalSalesforcePort {

    Sucursal findById(String id);
}
