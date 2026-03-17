package com.s1.LogiTrack.Service;

import com.s1.LogiTrack.Dto.Response.MovimientoDetalleResponse;

import java.util.List;

public interface MovimientoDetalleService {

    List<MovimientoDetalleResponse> obtenerPorMovimiento(Integer movimientoId);
}