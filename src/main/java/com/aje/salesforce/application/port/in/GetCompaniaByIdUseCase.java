package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.Compania;

public interface GetCompaniaByIdUseCase {
    
    Compania getById(String id);
}
