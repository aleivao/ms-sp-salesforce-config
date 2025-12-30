package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.application.port.in.GetTipoDocumentoByIdUseCase;
import com.aje.salesforce.application.port.in.GetTiposDocumentoByPaisUseCase;
import com.aje.salesforce.application.port.in.SyncTipoDocumentoUseCase;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.TipoDocumentoDto;
import com.aje.salesforce.infrastructure.adapter.in.rest.mapper.TipoDocumentoDtoMapper;
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
@RequestMapping("/api/v1/tipos-documento")
@RequiredArgsConstructor
@Tag(name = "Tipos de Documento", description = "API para gestion de tipos de documento desde Salesforce")
public class TipoDocumentoController {

    private final GetTipoDocumentoByIdUseCase getTipoDocumentoByIdUseCase;
    private final GetTiposDocumentoByPaisUseCase getTiposDocumentoByPaisUseCase;
    private final SyncTipoDocumentoUseCase syncTipoDocumentoUseCase;
    private final TipoDocumentoDtoMapper mapper;

    @Operation(
        summary = "Listar tipos de documento por pais",
        description = "Obtiene todos los tipos de documento de un pais especifico desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de documento obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TipoDocumentoDto.class))
            )
        ),
        @ApiResponse(responseCode = "400", description = "Parametro de pais invalido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<TipoDocumentoDto>> getTiposDocumentoByCountry(
        @Parameter(description = "Nombre del pais", required = true, example = "PE")
        @RequestParam("pais")
        @NotBlank(message = "El pais no puede estar vacio")
        String pais
    ) {
        log.info("GET /api/v1/tipos-documento - Fetching tipos documento for country: {}", pais);

        List<TipoDocumentoDto> tiposDocumento = mapper.toDtoList(
            getTiposDocumentoByPaisUseCase.getByPais(pais)
        );

        log.info("Returning {} tipos documento for country: {}", tiposDocumento.size(), pais);

        return ResponseEntity.ok(tiposDocumento);
    }

    @Operation(
        summary = "Obtener tipo de documento por ID",
        description = "Obtiene un tipo de documento especifico por su ID desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de documento encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoDocumentoDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentoDto> getTipoDocumentoById(
        @Parameter(description = "ID del tipo de documento", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacio")
        String id
    ) {
        log.info("GET /api/v1/tipos-documento/{} - Fetching tipo documento", id);

        TipoDocumentoDto tipoDocumento = mapper.toDto(
            getTipoDocumentoByIdUseCase.getById(id)
        );

        log.info("Returning tipo documento: {} ({})", tipoDocumento.getName(), tipoDocumento.getId());

        return ResponseEntity.ok(tipoDocumento);
    }

    @Operation(
        summary = "Sincronizar tipo de documento por ID",
        description = "Sincroniza un tipo de documento especifico desde Salesforce a la base de datos local"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sincronizacion completada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoDocumentoDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sync/{id}")
    public Mono<TipoDocumentoDto> syncTipoDocumentoById(
        @Parameter(description = "ID del tipo de documento", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacio")
        String id
    ) {
        log.info("POST /api/v1/tipos-documento/sync/{} - Syncing tipo documento", id);

        return syncTipoDocumentoUseCase.syncById(id)
                .map(mapper::toDto)
                .doOnSuccess(tipoDocumento -> log.info("Sync completed for tipo documento: {}", id));
    }
}
