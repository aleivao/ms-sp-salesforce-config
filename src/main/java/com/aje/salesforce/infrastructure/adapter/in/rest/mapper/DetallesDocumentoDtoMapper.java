package com.aje.salesforce.infrastructure.adapter.in.rest.mapper;

import com.aje.salesforce.domain.model.DetallesDocumento;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.DetallesDocumentoDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DetallesDocumentoDtoMapper {

    DetallesDocumentoDto toDto(DetallesDocumento detallesDocumento);

    List<DetallesDocumentoDto> toDtoList(List<DetallesDocumento> detallesDocumentos);
}
