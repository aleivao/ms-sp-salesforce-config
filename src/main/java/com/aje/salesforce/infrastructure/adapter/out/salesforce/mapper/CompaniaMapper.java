package com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper;

import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.CompaniaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompaniaMapper {
    
    @Mapping(target = "createdDate", source = "createdDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "systemModstamp", source = "systemModstamp", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastActivityDate", source = "lastActivityDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastViewedDate", source = "lastViewedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastReferencedDate", source = "lastReferencedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "codigo", source = "codigo")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "pais", source = "pais")
    @Mapping(target = "direccion", source = "direccion")
    @Mapping(target = "telefono", source = "telefono")
    @Mapping(target = "acronimo", source = "acronimo")
    @Mapping(target = "integracionErp", source = "integracionErp")
    @Mapping(target = "habilitarSeleccionComprobante", source = "habilitarSeleccionComprobante")
    @Mapping(target = "habilitarComprobantePreventa", source = "habilitarComprobantePreventa")
    @Mapping(target = "habilitarImpuestos", source = "habilitarImpuestos")
    @Mapping(target = "impuesto", source = "impuesto")
    @Mapping(target = "habilitarExtraModulo", source = "habilitarExtraModulo")
    @Mapping(target = "habilitarLineaDeCredito", source = "habilitarLineaDeCredito")
    @Mapping(target = "acceso", source = "acceso")
    @Mapping(target = "correoCompania", source = "correoCompania")
    @Mapping(target = "enviarAOracle", source = "enviarAOracle")
    @Mapping(target = "enviarASiesa", source = "enviarASiesa")
    @Mapping(target = "sellin", source = "sellin")
    Compania toDomain(CompaniaResponse response);
    
    List<Compania> toDomainList(List<CompaniaResponse> responses);
    
    @Named("zonedDateTimeToLocalDateTime")
    default LocalDateTime zonedDateTimeToLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toLocalDateTime() : null;
    }
}
