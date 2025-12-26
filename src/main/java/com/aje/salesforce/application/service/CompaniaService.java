package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.GetCompaniaByIdUseCase;
import com.aje.salesforce.application.port.in.GetCompaniasByPaisUseCase;
import com.aje.salesforce.application.port.out.SalesforcePort;
import com.aje.salesforce.domain.model.Compania;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompaniaService implements GetCompaniasByPaisUseCase, GetCompaniaByIdUseCase {
    
    private final SalesforcePort salesforcePort;
    
    @Override
    @Cacheable(value = "companiasByPais", key = "#pais")
    public List<Compania> getByPais(String pais) {
        log.info("Fetching companies for country: {}", pais);
        
        List<Compania> companias = salesforcePort.findByPais(pais);
        
        log.info("Found {} companies for country: {}", companias.size(), pais);
        
        return companias;
    }
    
    @Override
    @Cacheable(value = "companiaById", key = "#id")
    public Compania getById(String id) {
        log.info("Fetching company with ID: {}", id);
        
        Compania compania = salesforcePort.findById(id);
        
        log.info("Found company: {} ({})", compania.getName(), compania.getId());
        
        return compania;
    }
}
