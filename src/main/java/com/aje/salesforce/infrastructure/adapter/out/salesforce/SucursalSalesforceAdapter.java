package com.aje.salesforce.infrastructure.adapter.out.salesforce;

import com.aje.salesforce.application.port.out.SucursalSalesforcePort;
import com.aje.salesforce.domain.exception.SucursalNotFoundException;
import com.aje.salesforce.domain.model.Sucursal;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.client.SalesforceClient;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper.SucursalMapper;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.SucursalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SucursalSalesforceAdapter implements SucursalSalesforcePort {

    private final SalesforceClient salesforceClient;
    private final SucursalMapper mapper;

    @Override
    public Sucursal findById(String id) {
        log.info("Fetching sucursal from Salesforce with ID: {}", id);

        SucursalResponse response = salesforceClient.querySucursalById(id);

        if (response == null) {
            throw new SucursalNotFoundException(id);
        }

        Sucursal sucursal = mapper.toDomain(response);

        log.info("Fetched sucursal from Salesforce: {} ({})", sucursal.getName(), sucursal.getId());

        return sucursal;
    }
}
