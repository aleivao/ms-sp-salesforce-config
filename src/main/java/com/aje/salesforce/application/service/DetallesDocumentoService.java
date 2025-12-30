package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.GetDetallesDocumentoByConfiguracionUseCase;
import com.aje.salesforce.application.port.in.GetDetallesDocumentoByIdUseCase;
import com.aje.salesforce.application.port.out.DetallesDocumentoSalesforcePort;
import com.aje.salesforce.domain.model.DetallesDocumento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetallesDocumentoService implements GetDetallesDocumentoByIdUseCase, GetDetallesDocumentoByConfiguracionUseCase {

    private final DetallesDocumentoSalesforcePort salesforcePort;

    @Override
    public DetallesDocumento getById(String id) {
        log.info("Getting detalles documento by id: {}", id);
        return salesforcePort.findById(id);
    }

    @Override
    public List<DetallesDocumento> getByConfiguracion(String configuracionImpresionId) {
        log.info("Getting detalles documento by configuracion: {}", configuracionImpresionId);
        return salesforcePort.findByConfiguracionImpresion(configuracionImpresionId);
    }
}
