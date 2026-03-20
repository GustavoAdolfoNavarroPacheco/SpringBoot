package com.s1.LogiTrack.Service;

import com.s1.LogiTrack.Dto.Request.MovimientoRequest;
import com.s1.LogiTrack.Dto.Response.MovimientoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {

    MovimientoResponse crear(MovimientoRequest request, Integer usuarioId);
    MovimientoResponse obtenerPorId(Integer id);
    List<MovimientoResponse> obtenerTodos();
    List<MovimientoResponse> obtenerPorFechas(LocalDateTime inicio, LocalDateTime fin);
    List<MovimientoResponse> listarRecientes();
//    List<MovimientoResponse> cantidad(Integer id);
//    List<MovimientoResponse> cantidadPorMovimiento(String TipoMovimiento);
}