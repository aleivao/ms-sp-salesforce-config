package com.aje.salesforce.infrastructure.adapter.out.persistence.mapper;

import com.aje.salesforce.domain.model.TipoDocumento;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.TipoDocumentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TipoDocumentoEntityMapper {

    TipoDocumentoEntity toEntity(TipoDocumento domain);

    TipoDocumento toDomain(TipoDocumentoEntity entity);
}
