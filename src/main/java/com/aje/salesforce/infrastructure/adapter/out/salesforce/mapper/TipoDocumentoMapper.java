package com.aje.salesforce.infrastructure.adapter.out.salesforce.mapper;

import com.aje.salesforce.domain.model.TipoDocumento;
import com.aje.salesforce.infrastructure.adapter.out.salesforce.response.TipoDocumentoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TipoDocumentoMapper {

    @Mapping(target = "createdDate", source = "createdDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "systemModstamp", source = "systemModstamp", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastActivityDate", source = "lastActivityDate", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "lastViewedDate", source = "lastViewedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "lastReferencedDate", source = "lastReferencedDate", qualifiedByName = "zonedDateTimeToLocalDateTime")
    TipoDocumento toDomain(TipoDocumentoResponse response);

    List<TipoDocumento> toDomainList(List<TipoDocumentoResponse> responses);

    @Named("zonedDateTimeToLocalDateTime")
    default LocalDateTime zonedDateTimeToLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toLocalDateTime() : null;
    }

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
