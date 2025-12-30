package com.aje.salesforce.infrastructure.adapter.out.persistence.mapper;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.ConfiguracionImpresionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConfiguracionImpresionEntityMapper {

    ConfiguracionImpresionEntity toEntity(ConfiguracionImpresion domain);

    ConfiguracionImpresion toDomain(ConfiguracionImpresionEntity entity);
}
