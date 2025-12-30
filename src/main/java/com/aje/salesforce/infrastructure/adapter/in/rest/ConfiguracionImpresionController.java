package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.application.port.in.GetConfiguracionImpresionByIdUseCase;
import com.aje.salesforce.application.port.in.GetConfiguracionesImpresionByPaisUseCase;
import com.aje.salesforce.application.port.in.SyncConfiguracionImpresionUseCase;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.ConfiguracionImpresionDto;
import com.aje.salesforce.infrastructure.adapter.in.rest.mapper.ConfiguracionImpresionDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/configuraciones-impresion")
@RequiredArgsConstructor
@Tag(name = "Configuraciones de Impresion", description = "API para gestion de configuraciones de impresion desde Salesforce")
public class ConfiguracionImpresionController {

    private final GetConfiguracionImpresionByIdUseCase getConfiguracionImpresionByIdUseCase;
    private final GetConfiguracionesImpresionByPaisUseCase getConfiguracionesImpresionByPaisUseCase;
    private final SyncConfiguracionImpresionUseCase syncConfiguracionImpresionUseCase;
    private final ConfiguracionImpresionDtoMapper mapper;

    @Operation(
        summary = "Listar configuraciones de impresion por pais",
        description = "Obtiene todas las configuraciones de impresion de un pais especifico desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de configuraciones obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ConfiguracionImpresionDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parametro de pais invalido"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<List<ConfiguracionImpresionDto>> getConfiguracionesByCountry(
        @Parameter(description = "Nombre del pais", required = true, example = "PE")
        @RequestParam("pais")
        @NotBlank(message = "El pais no puede estar vacio")
        String pais
    ) {
        log.info("GET /api/v1/configuraciones-impresion - Fetching configuraciones for country: {}", pais);

        List<ConfiguracionImpresionDto> configuraciones = mapper.toDtoList(
            getConfiguracionesImpresionByPaisUseCase.getByPais(pais)
        );

        log.info("Returning {} configuraciones for country: {}", configuraciones.size(), pais);

        return ResponseEntity.ok(configuraciones);
    }

    @Operation(
        summary = "Obtener configuracion de impresion por ID",
        description = "Obtiene una configuracion de impresion especifica por su ID desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Configuracion encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ConfiguracionImpresionDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Configuracion no encontrada"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionImpresionDto> getConfiguracionById(
        @Parameter(description = "ID de la configuracion", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacio")
        String id
    ) {
        log.info("GET /api/v1/configuraciones-impresion/{} - Fetching configuracion", id);

        ConfiguracionImpresionDto configuracion = mapper.toDto(
            getConfiguracionImpresionByIdUseCase.getById(id)
        );

        log.info("Returning configuracion: {} ({})", configuracion.getName(), configuracion.getId());

        return ResponseEntity.ok(configuracion);
    }

    @Operation(
        summary = "Sincronizar configuracion de impresion por ID",
        description = "Sincroniza una configuracion de impresion especifica desde Salesforce a la base de datos local"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sincronizacion completada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ConfiguracionImpresionDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Configuracion no encontrada"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/sync/{id}")
    public Mono<ConfiguracionImpresionDto> syncConfiguracionById(
        @Parameter(description = "ID de la configuracion", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacio")
        String id
    ) {
        log.info("POST /api/v1/configuraciones-impresion/sync/{} - Syncing configuracion", id);

        return syncConfiguracionImpresionUseCase.syncById(id)
                .map(mapper::toDto)
                .doOnSuccess(configuracion -> log.info("Sync completed for configuracion: {}", id));
    }
}
