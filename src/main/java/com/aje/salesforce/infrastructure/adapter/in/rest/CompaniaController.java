package com.aje.salesforce.infrastructure.adapter.in.rest;

import com.aje.salesforce.application.port.in.GetCompaniaByIdUseCase;
import com.aje.salesforce.application.port.in.GetCompaniasByPaisUseCase;
import com.aje.salesforce.application.port.in.SyncCompaniasUseCase;
import com.aje.salesforce.infrastructure.adapter.in.rest.dto.CompaniaDto;
import com.aje.salesforce.infrastructure.adapter.in.rest.mapper.CompaniaDtoMapper;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/companias")
@RequiredArgsConstructor
@Tag(name = "Compañías", description = "API para gestión de compañías desde Salesforce")
public class CompaniaController {
    
    private final GetCompaniasByPaisUseCase getCompaniasByPaisUseCase;
    private final GetCompaniaByIdUseCase getCompaniaByIdUseCase;
    private final SyncCompaniasUseCase syncCompaniasUseCase;
    private final CompaniaDtoMapper mapper;
    
    @Operation(
        summary = "Listar compañías por país",
        description = "Obtiene todas las compañías de un país específico desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de compañías obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = CompaniaDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetro de país inválido"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<List<CompaniaDto>> getCompaniesByCountry(
        @Parameter(description = "Nombre del país", required = true, example = "Peru")
        @RequestParam("pais")
        @NotBlank(message = "El país no puede estar vacío")
        String pais
    ) {
        log.info("GET /api/v1/companias - Fetching companies for country: {}", pais);
        
        List<CompaniaDto> companias = mapper.toDtoList(
            getCompaniasByPaisUseCase.getByPais(pais)
        );
        
        log.info("Returning {} companies for country: {}", companias.size(), pais);
        
        return ResponseEntity.ok(companias);
    }
    
    @Operation(
        summary = "Obtener compañía por ID",
        description = "Obtiene una compañía específica por su ID desde Salesforce"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Compañía encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CompaniaDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Compañía no encontrada"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompaniaDto> getCompanyById(
        @Parameter(description = "ID de la compañía", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacío")
        String id
    ) {
        log.info("GET /api/v1/companias/{} - Fetching company", id);
        
        CompaniaDto compania = mapper.toDto(
            getCompaniaByIdUseCase.getById(id)
        );
        
        log.info("Returning company: {} ({})", compania.getName(), compania.getId());

        return ResponseEntity.ok(compania);
    }

    @Operation(
        summary = "Sincronizar compañías por país",
        description = "Sincroniza las compañías de un país desde Salesforce a la base de datos local"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sincronización completada exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = CompaniaDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetro de país inválido"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/sync")
    public Flux<CompaniaDto> syncCompaniesByCountry(
        @Parameter(description = "Nombre del país", required = true, example = "Peru")
        @RequestParam("pais")
        @NotBlank(message = "El país no puede estar vacío")
        String pais
    ) {
        log.info("POST /api/v1/companias/sync - Syncing companies for country: {}", pais);

        return syncCompaniasUseCase.syncByPais(pais)
                .map(mapper::toDto)
                .doOnComplete(() -> log.info("Sync completed for country: {}", pais));
    }

    @Operation(
        summary = "Sincronizar compañía por ID",
        description = "Sincroniza una compañía específica desde Salesforce a la base de datos local"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sincronización completada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CompaniaDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Compañía no encontrada"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PostMapping("/sync/{id}")
    public Mono<CompaniaDto> syncCompanyById(
        @Parameter(description = "ID de la compañía", required = true, example = "a0X5f000000AbCdEFG")
        @PathVariable
        @NotBlank(message = "El ID no puede estar vacío")
        String id
    ) {
        log.info("POST /api/v1/companias/sync/{} - Syncing company", id);

        return syncCompaniasUseCase.syncById(id)
                .map(mapper::toDto)
                .doOnSuccess(compania -> log.info("Sync completed for company: {}", id));
    }
}
