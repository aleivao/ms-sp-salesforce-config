package com.aje.salesforce.infrastructure.adapter.in.rest.mapper;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.ConfiguracionImpresionDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConfiguracionImpresionDtoMapper {

    ConfiguracionImpresionDto toDto(ConfiguracionImpresion configuracionImpresion);

    List<ConfiguracionImpresionDto> toDtoList(List<ConfiguracionImpresion> configuraciones);
}
