package com.aje.salesforce.infrastructure.adapter.in.rest.mapper;

import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.CompaniaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompaniaDtoMapper {
    
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
    CompaniaDto toDto(Compania compania);
    
    List<CompaniaDto> toDtoList(List<Compania> companias);
}
