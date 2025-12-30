package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.GetConfiguracionImpresionByIdUseCase;
import com.aje.salesforce.application.port.in.GetConfiguracionesImpresionByPaisUseCase;
import com.aje.salesforce.application.port.out.ConfiguracionImpresionSalesforcePort;
import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguracionImpresionService implements GetConfiguracionImpresionByIdUseCase, GetConfiguracionesImpresionByPaisUseCase {

    private final ConfiguracionImpresionSalesforcePort configuracionImpresionSalesforcePort;

    @Override
    @Cacheable(value = "configuracionImpresionById", key = "#id")
    public ConfiguracionImpresion getById(String id) {
        log.info("Fetching configuracion impresion with ID: {}", id);

        ConfiguracionImpresion configuracion = configuracionImpresionSalesforcePort.findById(id);

        log.info("Found configuracion impresion: {} ({})", configuracion.getName(), configuracion.getId());

        return configuracion;
    }

    @Override
    @Cacheable(value = "configuracionesImpresionByPais", key = "#pais")
    public List<ConfiguracionImpresion> getByPais(String pais) {
        log.info("Fetching configuraciones impresion for country: {}", pais);

        List<ConfiguracionImpresion> configuraciones = configuracionImpresionSalesforcePort.findByPais(pais);

        log.info("Found {} configuraciones impresion for country: {}", configuraciones.size(), pais);

        return configuraciones;
    }
}
