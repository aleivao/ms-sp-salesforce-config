package com.aje.salesforce.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Sucursal de Salesforce")
public class SucursalDto {

    @Schema(description = "ID único de la sucursal", example = "a0Y5f000000AbCdEFG")
    private String id;

    @Schema(description = "Indica si está eliminado", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Nombre de la sucursal", example = "Sucursal Lima Norte")
    private String name;

    @Schema(description = "Código ISO de moneda", example = "PEN")
    private String currencyIsoCode;

    @Schema(description = "Fecha de creación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @Schema(description = "ID del creador")
    private String createdById;

    @Schema(description = "Fecha de última modificación")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "ID del último modificador")
    private String lastModifiedById;

    @Schema(description = "Timestamp de última modificación del sistema")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime systemModstamp;

    @Schema(description = "Fecha de última actividad")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastActivityDate;

    @Schema(description = "Fecha de última visualización")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastViewedDate;

    @Schema(description = "Fecha de última referencia")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastReferencedDate;

    @Schema(description = "ID de la compañía relacionada")
    private String compania;

    @Schema(description = "Código de la sucursal", example = "SUC001")
    private String codigo;

    @Schema(description = "Código de compañía")
    private String codigoDeCompania;

    @Schema(description = "Código único")
    private String codigoUnico;

    @Schema(description = "País de la sucursal", example = "Peru")
    private String pais;

    @Schema(description = "ID del almacenista")
    private String almacenista;

    @Schema(description = "ID del eje territorial")
    private String ejeTerritorial;

    @Schema(description = "Habilitar módulo extra")
    private Boolean habilitarExtraModulo;

    @Schema(description = "Habilitar línea de crédito")
    private Boolean habilitarLineaDeCredito;

    @Schema(description = "Habilitar transferencia gratuita")
    private Boolean habilitarTransferenciaGratuita;

    @Schema(description = "Tipo de sucursal")
    private String tipoDeSucursal;

    @Schema(description = "Enviar a Oracle")
    private Boolean enviarAOracle;

    @Schema(description = "Almacén")
    private String almacen;

    @Schema(description = "ID de bloques de envío de pedido")
    private String bloquesDeEnvioDePedido;

    @Schema(description = "Enviar por bloques")
    private Boolean enviarPorBloques;

    @Schema(description = "Bloquear envío ERP")
    private Boolean bloquearEnvioErp;

    @Schema(description = "Límite de reenvío de pedidos")
    private Double limiteReenvioPedidos;

    @Schema(description = "Minutos de reenvío de pedido en espera")
    private Double minutosReenvioPedidoEnEspera;

    @Schema(description = "Reenvío de pedidos en espera")
    private Boolean reenvioPedidosEnEspera;

    @Schema(description = "Minutos de reenvío de pedido por defecto")
    private Double minutosReenvioPedidoDefault;

    @Schema(description = "Facturación punto de venta")
    private Boolean facturacionPuntoDeVenta;

    @Schema(description = "Estado de la sucursal", example = "Activo")
    private String estado;

    @Schema(description = "Enviar a Siesa")
    private Boolean enviarASiesa;
}
