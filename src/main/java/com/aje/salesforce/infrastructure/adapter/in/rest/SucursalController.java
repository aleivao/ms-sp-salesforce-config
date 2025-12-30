package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.application.port.in.GetSucursalByIdUseCase;
import com.aje.salesforce.application.port.in.SyncSucursalUseCase;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.SucursalDto;
import com.aje.salesforce.infrastructure.adapter.in.rest.mapper.SucursalDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "API para gestión de sucursales desde Salesforce")
public class SucursalController {

    private final GetSucursalByIdUseCase getSucursalByIdUseCase;
    private final SyncSucursalUseCase syncSucursalUseCase;
    private final SucursalDtoMapper mapper;

    @Operation(
        summary = "Obtener sucursal por ID",
        description = "Obtiene una sucursal específica por su ID desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sucursal encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SucursalDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sucursal no encontrada"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDto> getSucursalById(
        @Parameter(description = "ID de la sucursal", required = true, example = "a0Y5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacío")
        String id
    ) {
        log.info("GET /api/v1/sucursales/{} - Fetching sucursal", id);

        SucursalDto sucursal = mapper.toDto(
            getSucursalByIdUseCase.getById(id)
        );

        log.info("Returning sucursal: {} ({})", sucursal.getName(), sucursal.getId());

        return ResponseEntity.ok(sucursal);
    }

    @Operation(
        summary = "Sincronizar sucursal por ID",
        description = "Sincroniza una sucursal específica desde Salesforce a la base de datos local"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sincronización completada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SucursalDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sucursal no encontrada"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/sync/{id}")
    public Mono<SucursalDto> syncSucursalById(
        @Parameter(description = "ID de la sucursal", required = true, example = "a0Y5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacío")
        String id
    ) {
        log.info("POST /api/v1/sucursales/sync/{} - Syncing sucursal", id);

        return syncSucursalUseCase.syncById(id)
                .map(mapper::toDto)
                .doOnSuccess(sucursal -> log.info("Sync completed for sucursal: {}", id));
    }
}
