package com.s1.LogiTrack.Service;

import com.s1.LogiTrack.Dto.Response.AuditoriaResponse;
import com.s1.LogiTrack.Model.TipoOperacion;

import java.util.List;

public interface AuditoriaService {

    void registrar(TipoOperacion operacion, String entidad, String descripcion, Integer usuarioId);
    List<AuditoriaResponse> obtenerTodos();
    List<AuditoriaResponse> obtenerPorOperacion(TipoOperacion operacion);
    List<AuditoriaResponse> obtenerPorUsuario(Integer usuarioId);
}