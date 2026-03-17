package com.s1.LogiTrack.Dto.Response;

import com.s1.LogiTrack.Model.TipoMovimiento;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovimientoResponse {

    private Integer id;
    private TipoMovimiento tipoMovimiento;
    private String descripcion;
    private LocalDateTime fecha;
    private String usuarioNombre;
    private String bodegaOrigenNombre;
    private String bodegaDestinoNombre;
    private List<MovimientoDetalleResponse> detalles;
}