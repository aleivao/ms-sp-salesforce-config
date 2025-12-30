package com.aje.salesforce.application.service;

import com.aje.salesforce.application.port.in.GetSucursalByIdUseCase;
import com.aje.salesforce.application.port.out.SucursalSalesforcePort;
import com.aje.salesforce.domain.model.Sucursal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalService implements GetSucursalByIdUseCase {

    private final SucursalSalesforcePort sucursalSalesforcePort;

    @Override
    @Cacheable(value = "sucursalById", key = "#id")
    public Sucursal getById(String id) {
        log.info("Fetching sucursal with ID: {}", id);

        Sucursal sucursal = sucursalSalesforcePort.findById(id);

        log.info("Found sucursal: {} ({})", sucursal.getName(), sucursal.getId());

        return sucursal;
    }
}
