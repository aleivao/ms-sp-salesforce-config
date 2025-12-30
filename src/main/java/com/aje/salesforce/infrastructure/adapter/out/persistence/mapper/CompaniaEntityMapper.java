package com.aje.salesforce.infrastructure.adapter.out.persistence.mapper;

import com.aje.salesforce.domain.model.Compania;
import com.aje.salesforce.infrastructure.adapter.out.persistence.entity.CompaniaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompaniaEntityMapper {

    @Mapping(target = "integracionErp", source = "integracionErp", qualifiedByName = "stringToBoolean")
    @Mapping(target = "impuesto", source = "impuesto", qualifiedByName = "stringToDouble")
    @Mapping(target = "lastActivityDate", source = "lastActivityDate", qualifiedByName = "localDateTimeToLocalDate")
    CompaniaEntity toEntity(Compania domain);

    @Mapping(target = "integracionErp", source = "integracionErp", qualifiedByName = "booleanToString")
    @Mapping(target = "impuesto", source = "impuesto", qualifiedByName = "doubleToString")
    @Mapping(target = "lastActivityDate", source = "lastActivityDate", qualifiedByName = "localDateToLocalDateTime")
    Compania toDomain(CompaniaEntity entity);

    @Named("stringToBoolean")
    default Boolean stringToBoolean(String value) {
        if (value == null) return null;
        return "true".equalsIgnoreCase(value) || "1".equals(value) || "yes".equalsIgnoreCase(value);
    }

    @Named("booleanToString")
    default String booleanToString(Boolean value) {
        return value != null ? value.toString() : null;
    }

    @Named("stringToDouble")
    default Double stringToDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Named("doubleToString")
    default String doubleToString(Double value) {
        return value != null ? value.toString() : null;
    }

    @Named("localDateTimeToLocalDate")
    default LocalDate localDateTimeToLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    @Named("localDateToLocalDateTime")
    default LocalDateTime localDateToLocalDateTime(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }
}
