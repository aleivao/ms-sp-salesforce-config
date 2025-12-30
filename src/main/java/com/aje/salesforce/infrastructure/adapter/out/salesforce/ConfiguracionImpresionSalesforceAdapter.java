package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.application.port.out.ConfiguracionImpresionSalesforcePort;
import com.aje.salesforce.domain.exception.ConfiguracionImpresionNotFoundException;
import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.ConfiguracionImpresionMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.ConfiguracionImpresionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfiguracionImpresionSalesforceAdapter implements ConfiguracionImpresionSalesforcePort {

    private final SalesforceClient salesforceClient;
    private final ConfiguracionImpresionMapper mapper;

    @Override
    public ConfiguracionImpresion findById(String id) {
        log.info("Fetching configuracion impresion from Salesforce with ID: {}", id);

        ConfiguracionImpresionResponse response = salesforceClient.queryConfiguracionImpresionById(id);

        if (response == null) {
            throw new ConfiguracionImpresionNotFoundException(id);
        }

        ConfiguracionImpresion configuracion = mapper.toDomain(response);

        log.info("Fetched configuracion impresion from Salesforce: {} ({})", configuracion.getName(), configuracion.getId());

        return configuracion;
    }

    @Override
    public List<ConfiguracionImpresion> findByPais(String pais) {
        log.info("Fetching configuraciones impresion from Salesforce for country: {}", pais);

        List<ConfiguracionImpresionResponse> responses = salesforceClient.queryConfiguracionImpresionByPais(pais);

        List<ConfiguracionImpresion> configuraciones = mapper.toDomainList(responses);

        log.info("Fetched {} configuraciones impresion from Salesforce for country: {}", configuraciones.size(), pais);

        return configuraciones;
    }
}
