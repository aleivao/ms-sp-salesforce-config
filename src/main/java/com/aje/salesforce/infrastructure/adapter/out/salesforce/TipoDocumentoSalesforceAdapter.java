package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.application.port.out.TipoDocumentoSalesforcePort;
import com.aje.salesforce.domain.exception.TipoDocumentoNotFoundException;
import com.aje.salesforce.domain.model.TipoDocumento;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.TipoDocumentoMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.TipoDocumentoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TipoDocumentoSalesforceAdapter implements TipoDocumentoSalesforcePort {

    private final SalesforceClient salesforceClient;
    private final TipoDocumentoMapper mapper;

    @Override
    public TipoDocumento findById(String id) {
        log.info("Fetching tipo documento from Salesforce with ID: {}", id);

        TipoDocumentoResponse response = salesforceClient.queryTipoDocumentoById(id);

        if (response == null) {
            throw new TipoDocumentoNotFoundException(id);
        }

        TipoDocumento tipoDocumento = mapper.toDomain(response);

        log.info("Fetched tipo documento from Salesforce: {} ({})", tipoDocumento.getName(), tipoDocumento.getId());

        return tipoDocumento;
    }

    @Override
    public List<TipoDocumento> findByPais(String pais) {
        log.info("Fetching tipos documento from Salesforce for country: {}", pais);

        List<TipoDocumentoResponse> responses = salesforceClient.queryTipoDocumentoByPais(pais);

        List<TipoDocumento> tiposDocumento = mapper.toDomainList(responses);

        log.info("Fetched {} tipos documento from Salesforce for country: {}", tiposDocumento.size(), pais);

        return tiposDocumento;
    }
}
