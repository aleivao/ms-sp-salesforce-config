package com.aje.salesforce.infrastructure.adapter.in.rest.mapper;

import com.aje.salesforce.domain.model.Sucursal;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.SucursalDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SucursalDtoMapper {

    SucursalDto toDto(Sucursal sucursal);
}
