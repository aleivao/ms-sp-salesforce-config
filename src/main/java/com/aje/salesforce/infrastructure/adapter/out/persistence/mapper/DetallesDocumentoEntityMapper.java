package com.aje.salesforce.infrastructure.adapter.out.persistence.mapper;

import com.aje.salesforce.domain.model.DetallesDocumento;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.DetallesDocumentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DetallesDocumentoEntityMapper {

    DetallesDocumentoEntity toEntity(DetallesDocumento domain);

    DetallesDocumento toDomain(DetallesDocumentoEntity entity);

    List<DetallesDocumento> toDomainList(List<DetallesDocumentoEntity> entities);
}
