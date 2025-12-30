package com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper;

import com.aje.salesforce.domain.model.ConfiguracionImpresion;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.ConfiguracionImpresionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConfiguracionImpresionMapper {

    @Mapping(target = "createdDate", source = "createdDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "systemModstamp", source = "systemModstamp", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastViewedDate", source = "lastViewedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastReferencedDate", source = "lastReferencedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    ConfiguracionImpresion toDomain(ConfiguracionImpresionResponse response);

    List<ConfiguracionImpresion> toDomainList(List<ConfiguracionImpresionResponse> responses);

    @Named("zonedDateTimeToLocalDateTime")
    default LocalDateTime zonedDateTimeToLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toLocalDateTime() : null;
    }
}
