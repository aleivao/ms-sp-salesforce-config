package com.aje.salesforce.domain.model;

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
public class Sucursal {

    private String id;
    private Boolean isDeleted;
    private String name;
    private String currencyIsoCode;

    private LocalDateTime createdDate;
    private String createdById;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedById;

    private LocalDateTime systemModstamp;
    private LocalDate lastActivityDate;
    private LocalDateTime lastViewedDate;
    private LocalDateTime lastReferencedDate;

    private String compania;
    private String codigo;
    private String codigoDeCompania;
    private String codigoUnico;
    private String pais;
    private String almacenista;
    private String ejeTerritorial;

    private Boolean habilitarExtraModulo;
    private Boolean habilitarLineaDeCredito;
    private Boolean habilitarTransferenciaGratuita;

    private String tipoDeSucursal;
    private Boolean enviarAOracle;
    private String almacen;
    private String bloquesDeEnvioDePedido;
    private Boolean enviarPorBloques;
    private Boolean bloquearEnvioErp;

    private Double limiteReenvioPedidos;
    private Double minutosReenvioPedidoEnEspera;
    private Boolean reenvioPedidosEnEspera;
    private Double minutosReenvioPedidoDefault;

    private Boolean facturacionPuntoDeVenta;
    private String estado;
    private Boolean enviarASiesa;

    public boolean isActive() {
        return Boolean.FALSE.equals(isDeleted) && "Activo".equalsIgnoreCase(estado);
    }
}
