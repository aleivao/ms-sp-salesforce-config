package com.aje.salesforce.application.port.in;

import com.aje.salesforce.domain.model.Compania;

import java.util.List;

public interface GetCompaniasByPaisUseCase {
    
    List<Compania> getByPais(String pais);
}
