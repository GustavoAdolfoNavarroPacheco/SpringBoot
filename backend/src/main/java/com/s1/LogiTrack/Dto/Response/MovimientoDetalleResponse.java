package com.s1.LogiTrack.Dto.Response;

import lombok.Data;

@Data
public class MovimientoDetalleResponse {

    private Integer id;
    private Integer productoId;
    private String productoNombre;
    private Integer cantidad;
}