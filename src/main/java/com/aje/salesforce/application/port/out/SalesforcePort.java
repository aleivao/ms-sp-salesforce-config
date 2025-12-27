package com.aje.salesforce.application.port.out;

import com.aje.salesforce.domain.model.Compania;

import java.util.List;

public interface SalesforcePort {
    
    List<Compania> findByPais(String pais);
    
    Compania findById(String id);
}
