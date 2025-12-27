package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.application.port.out.SalesforcePort;
import com.aje.salesforce.domain.exception.CompaniaNotFoundException;
import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.CompaniaMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.CompaniaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesforceAdapter implements SalesforcePort {
    
    private final SalesforceClient salesforceClient;
    private final CompaniaMapper mapper;
    
    @Override
    public List<Compania> findByPais(String pais) {
        log.info("Fetching companies from Salesforce for country: {}", pais);
        
        List<CompaniaResponse> responses = salesforceClient.queryByPais(pais);
        
        List<Compania> companias = mapper.toDomainList(responses);
        
        log.info("Fetched {} companies from Salesforce for country: {}", companias.size(), pais);
        
        return companias;
    }
    
    @Override
    public Compania findById(String id) {
        log.info("Fetching company from Salesforce with ID: {}", id);
        
        CompaniaResponse response = salesforceClient.queryById(id);
        
        if (response == null) {
            throw new CompaniaNotFoundException(id);
        }
        
        Compania compania = mapper.toDomain(response);
        
        log.info("Fetched company from Salesforce: {} ({})", compania.getName(), compania.getId());
        
        return compania;
    }
}
