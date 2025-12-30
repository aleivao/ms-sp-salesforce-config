package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.application.port.in.GetDetallesDocumentoByConfiguracionUseCase;
import com.aje.salesforce.application.port.in.GetDetallesDocumentoByIdUseCase;
import com.aje.salesforce.application.port.in.SyncDetallesDocumentoUseCase;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.DetallesDocumentoDto;
import com.aje.salesforce.infrastructure.adapter.in.rest.mapper.DetallesDocumentoDtoMapper;
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
@RequestMapping("/api/v1/detalles-documento")
@RequiredArgsConstructor
@Tag(name = "Detalles de Documento", description = "API para gestion de detalles de documento desde Salesforce")
public class DetallesDocumentoController {

    private final GetDetallesDocumentoByIdUseCase getDetallesDocumentoByIdUseCase;
    private final GetDetallesDocumentoByConfiguracionUseCase getDetallesDocumentoByConfiguracionUseCase;
    private final SyncDetallesDocumentoUseCase syncDetallesDocumentoUseCase;
    private final DetallesDocumentoDtoMapper mapper;

    @Operation(
        summary = "Listar detalles de documento por configuracion de impresion",
        description = "Obtiene todos los detalles de documento de una configuracion de impresion especifica desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de detalles de documento obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = DetallesDocumentoDto.class))
            )
        ),
        @ApiResponse(responseCode = "400", description = "Parametro de configuracion invalido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<DetallesDocumentoDto>> getDetallesDocumentoByConfiguracion(
        @Parameter(description = "ID de configuracion de impresion", required = true, example = "a0X5f000000AbCdEFG")
        @RequestParam("configuracionImpresionId")
        @NotBlank(message = "El ID de configuracion de impresion no puede estar vacio")
        String configuracionImpresionId
    ) {
        log.info("GET /api/v1/detalles-documento - Fetching detalles documento for configuracion: {}", configuracionImpresionId);

        List<DetallesDocumentoDto> detallesDocumentos = mapper.toDtoList(
            getDetallesDocumentoByConfiguracionUseCase.getByConfiguracion(configuracionImpresionId)
        );

        log.info("Returning {} detalles documento for configuracion: {}", detallesDocumentos.size(), configuracionImpresionId);

        return ResponseEntity.ok(detallesDocumentos);
    }

    @Operation(
        summary = "Obtener detalle de documento por ID",
        description = "Obtiene un detalle de documento especifico por su ID desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Detalle de documento encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DetallesDocumentoDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Detalle de documento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetallesDocumentoDto> getDetallesDocumentoById(
        @Parameter(description = "ID del detalle de documento", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacio")
        String id
    ) {
        log.info("GET /api/v1/detalles-documento/{} - Fetching detalle documento", id);

        DetallesDocumentoDto detallesDocumento = mapper.toDto(
            getDetallesDocumentoByIdUseCase.getById(id)
        );

        log.info("Returning detalle documento: {} ({})", detallesDocumento.getName(), detallesDocumento.getId());

        return ResponseEntity.ok(detallesDocumento);
    }

    @Operation(
        summary = "Sincronizar detalle de documento por ID",
        description = "Sincroniza un detalle de documento especifico desde Salesforce a la base de datos local"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sincronizacion completada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DetallesDocumentoDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Detalle de documento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sync/{id}")
    public Mono<DetallesDocumentoDto> syncDetallesDocumentoById(
        @Parameter(description = "ID del detalle de documento", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacio")
        String id
    ) {
        log.info("POST /api/v1/detalles-documento/sync/{} - Syncing detalle documento", id);

        return syncDetallesDocumentoUseCase.syncById(id)
                .map(mapper::toDto)
                .doOnSuccess(detallesDocumento -> log.info("Sync completed for detalle documento: {}", id));
    }
}
