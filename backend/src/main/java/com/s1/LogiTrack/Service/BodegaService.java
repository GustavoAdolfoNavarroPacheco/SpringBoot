package com.s1.LogiTrack.Service;

import com.s1.LogiTrack.Dto.Request.BodegaRequest;
import com.s1.LogiTrack.Dto.Response.BodegaResponse;

import java.util.List;

public interface BodegaService {

    BodegaResponse crear(BodegaRequest request);
    BodegaResponse obtenerPorId(Integer id);
    List<BodegaResponse> obtenerTodos();
    BodegaResponse actualizar(Integer id, BodegaRequest request);
    void eliminar(Integer id);
}