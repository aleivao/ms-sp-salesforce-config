package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.GetSucursalByIdUseCase;
import com.aje.salesforce.application.port.in.GetSucursalesByPaisUseCase;
import com.aje.salesforce.application.port.out.SucursalSalesforcePort;
import com.aje.salesforce.domain.model.Sucursal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalService implements GetSucursalByIdUseCase, GetSucursalesByPaisUseCase {

    private final SucursalSalesforcePort sucursalSalesforcePort;

    @Override
    @Cacheable(value = "sucursalById", key = "#id")
    public Sucursal getById(String id) {
        log.info("Fetching sucursal with ID: {}", id);

        Sucursal sucursal = sucursalSalesforcePort.findById(id);

        log.info("Found sucursal: {} ({})", sucursal.getName(), sucursal.getId());

        return sucursal;
    }

    @Override
    @Cacheable(value = "sucursalesByPais", key = "#pais")
    public List<Sucursal> getByPais(String pais) {
        log.info("Fetching sucursales for country: {}", pais);

        List<Sucursal> sucursales = sucursalSalesforcePort.findByPais(pais);

        log.info("Found {} sucursales for country: {}", sucursales.size(), pais);

        return sucursales;
    }
}
