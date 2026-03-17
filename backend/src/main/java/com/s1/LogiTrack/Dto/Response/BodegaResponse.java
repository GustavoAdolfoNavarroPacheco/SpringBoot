package com.s1.LogiTrack.Dto.Response;

import lombok.Data;

@Data
public class BodegaResponse {

    private Integer id;
    private String nombre;
    private String ubicacion;
    private Integer capacidad;
    private Integer encargadoId;
    private String encargadoNombre;
}