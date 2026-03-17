package com.s1.LogiTrack.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.s1.LogiTrack.Model.TipoMovimiento;

import java.util.List;

@Data
public class MovimientoRequest {

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    private String descripcion;
    private Integer bodegaOrigenId;
    private Integer bodegaDestinoId;

    @NotNull(message = "Debe incluir al menos un detalle")
    private List<MovimientoDetalleRequest> detalles;
}