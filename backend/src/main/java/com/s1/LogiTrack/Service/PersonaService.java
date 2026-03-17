package com.s1.LogiTrack.Service;

import com.s1.LogiTrack.Dto.Request.PersonaRequest;
import com.s1.LogiTrack.Dto.Response.PersonaResponse;

import java.util.List;

public interface PersonaService {

    PersonaResponse crear(PersonaRequest request);
    PersonaResponse obtenerPorId(Integer id);
    List<PersonaResponse> obtenerTodos();
    PersonaResponse actualizar(Integer id, PersonaRequest request);
    void eliminar(Integer id);
}