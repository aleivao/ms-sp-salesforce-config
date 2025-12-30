package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.application.port.out.DetallesDocumentoSalesforcePort;
import com.aje.salesforce.domain.exception.DetallesDocumentoNotFoundException;
import com.aje.salesforce.domain.model.DetallesDocumento;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.DetallesDocumentoMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.DetallesDocumentoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetallesDocumentoSalesforceAdapter implements DetallesDocumentoSalesforcePort {

    private final SalesforceClient salesforceClient;
    private final DetallesDocumentoMapper mapper;

    @Override
    @Cacheable(value = "detallesDocumentoById", key = "#id")
    public DetallesDocumento findById(String id) {
        log.info("Fetching detalles documento from Salesforce with id: {}", id);

        DetallesDocumentoResponse response = salesforceClient.queryDetallesDocumentoById(id);
        if (response == null) {
            throw new DetallesDocumentoNotFoundException("Detalles documento no encontrado con ID: " + id);
        }

        DetallesDocumento detallesDocumento = mapper.toDomain(response);
        log.info("Retrieved detalles documento: {} ({})", detallesDocumento.getName(), detallesDocumento.getId());
        return detallesDocumento;
    }

    @Override
    @Cacheable(value = "detallesDocumentoByConfiguracion", key = "#configuracionImpresionId")
    public List<DetallesDocumento> findByConfiguracionImpresion(String configuracionImpresionId) {
        log.info("Fetching detalles documento from Salesforce for configuracion: {}", configuracionImpresionId);

        List<DetallesDocumentoResponse> responses = salesforceClient.queryDetallesDocumentoByConfiguracion(configuracionImpresionId);
        List<DetallesDocumento> detallesDocumentos = mapper.toDomainList(responses);

        log.info("Retrieved {} detalles documento for configuracion: {}", detallesDocumentos.size(), configuracionImpresionId);
        return detallesDocumentos;
    }
}
