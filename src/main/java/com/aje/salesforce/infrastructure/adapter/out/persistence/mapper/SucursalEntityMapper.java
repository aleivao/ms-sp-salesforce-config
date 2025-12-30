package com.aje.salesforce.infrastructure.adapter.out.persistence.mapper;

import com.aje.salesforce.domain.model.Sucursal;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SucursalEntityMapper {

    SucursalEntity toEntity(Sucursal domain);

    Sucursal toDomain(SucursalEntity entity);
}
