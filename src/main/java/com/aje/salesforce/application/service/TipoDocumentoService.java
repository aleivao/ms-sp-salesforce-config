package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.GetTipoDocumentoByIdUseCase;
import com.aje.salesforce.application.port.in.GetTiposDocumentoByPaisUseCase;
import com.aje.salesforce.application.port.out.TipoDocumentoSalesforcePort;
import com.aje.salesforce.domain.model.TipoDocumento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoDocumentoService implements GetTipoDocumentoByIdUseCase, GetTiposDocumentoByPaisUseCase {

    private final TipoDocumentoSalesforcePort tipoDocumentoSalesforcePort;

    @Override
    @Cacheable(value = "tipoDocumentoById", key = "#id")
    public TipoDocumento getById(String id) {
        log.info("Fetching tipo documento with ID: {}", id);

        TipoDocumento tipoDocumento = tipoDocumentoSalesforcePort.findById(id);

        log.info("Found tipo documento: {} ({})", tipoDocumento.getName(), tipoDocumento.getId());

        return tipoDocumento;
    }

    @Override
    @Cacheable(value = "tiposDocumentoByPais", key = "#pais")
    public List<TipoDocumento> getByPais(String pais) {
        log.info("Fetching tipos documento for country: {}", pais);

        List<TipoDocumento> tiposDocumento = tipoDocumentoSalesforcePort.findByPais(pais);

        log.info("Found {} tipos documento for country: {}", tiposDocumento.size(), pais);

        return tiposDocumento;
    }
}
