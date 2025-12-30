package com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper;

import com.aje.salesforce.domain.model.DetallesDocumento;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.DetallesDocumentoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DetallesDocumentoMapper {

    @Mapping(target = "createdDate", source = "createdDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Mapping(target = "systemModstamp", source = "systemModstamp", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    DetallesDocumento toDomain(DetallesDocumentoResponse response);

    List<DetallesDocumento> toDomainList(List<DetallesDocumentoResponse> responses);
}
