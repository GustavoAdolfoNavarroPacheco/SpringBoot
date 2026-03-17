package com.s1.LogiTrack.Dto.Response;

import com.s1.LogiTrack.Model.TipoOperacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditoriaResponse {

    private Integer id;
    private String entidad;
    private TipoOperacion operacion;
    private LocalDateTime fecha;
    private Integer usuarioId;
    private String usuarioNombre;
    private String valorAnterior;
    private String valorNuevo;
}