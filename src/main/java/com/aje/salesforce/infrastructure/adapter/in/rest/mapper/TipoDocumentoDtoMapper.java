package com.aje.salesforce.infrastructure.adapter.in.rest.mapper;

import com.aje.salesforce.domain.model.TipoDocumento;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.TipoDocumentoDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TipoDocumentoDtoMapper {

    TipoDocumentoDto toDto(TipoDocumento tipoDocumento);

    List<TipoDocumentoDto> toDtoList(List<TipoDocumento> tiposDocumento);
}
