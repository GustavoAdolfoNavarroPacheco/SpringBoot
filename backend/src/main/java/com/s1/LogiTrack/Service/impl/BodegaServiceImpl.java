package com.s1.LogiTrack.Service.impl;

import com.s1.LogiTrack.Dto.Request.BodegaRequest;
import com.s1.LogiTrack.Dto.Response.BodegaResponse;
import com.s1.LogiTrack.Model.*;
import com.s1.LogiTrack.Repository.BodegaRepository;
import com.s1.LogiTrack.Repository.PersonaRepository;
import com.s1.LogiTrack.Security.SecurityUtils;
import com.s1.LogiTrack.Service.AuditoriaService;
import com.s1.LogiTrack.Service.BodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;
    private final PersonaRepository personaRepository;
    private final AuditoriaService auditoriaService;
    private final SecurityUtils securityUtils;

    @Override
    public BodegaResponse crear(BodegaRequest request) {
        Persona encargado = personaRepository.findById(request.getEncargadoId())
                .orElseThrow(() -> new RuntimeException("Encargado no encontrado"));
        Bodega bodega = new Bodega();
        bodega.setNombre(request.getNombre());
        bodega.setUbicacion(request.getUbicacion());
        bodega.setCapacidad(request.getCapacidad());
        bodega.setEncargado(encargado);
        Bodega saved = bodegaRepository.save(bodega);

        Persona usuario = securityUtils.getUsuarioActual();
        auditoriaService.registrar(
                TipoOperacion.CREAR,
                "Bodega",
                "Se creó la bodega: " + saved.getNombre(),
                usuario.getId()
        );

        return toResponse(saved);
    }

    @Override
    public BodegaResponse obtenerPorId(Integer id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        return toResponse(bodega);
    }

    @Override
    public List<BodegaResponse> obtenerTodos() {
        return bodegaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BodegaResponse actualizar(Integer id, BodegaRequest request) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        Persona encargado = personaRepository.findById(request.getEncargadoId())
                .orElseThrow(() -> new RuntimeException("Encargado no encontrado"));
        bodega.setNombre(request.getNombre());
        bodega.setUbicacion(request.getUbicacion());
        bodega.setCapacidad(request.getCapacidad());
        bodega.setEncargado(encargado);
        Bodega saved = bodegaRepository.save(bodega);

        Persona usuario = securityUtils.getUsuarioActual();
        auditoriaService.registrar(
                TipoOperacion.ACTUALIZAR,
                "Bodega",
                "Se actualizó la bodega: " + saved.getNombre(),
                usuario.getId()
        );

        return toResponse(saved);
    }

    @Override
    public void eliminar(Integer id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

        Persona usuario = securityUtils.getUsuarioActual();
        auditoriaService.registrar(
                TipoOperacion.ELIMINAR,
                "Bodega",
                "Se eliminó la bodega: " + bodega.getNombre(),
                usuario.getId()
        );

        bodegaRepository.deleteById(id);
    }

    private BodegaResponse toResponse(Bodega bodega) {
        BodegaResponse response = new BodegaResponse();
        response.setId(bodega.getId());
        response.setNombre(bodega.getNombre());
        response.setUbicacion(bodega.getUbicacion());
        response.setCapacidad(bodega.getCapacidad());
        response.setEncargadoId(bodega.getEncargado().getId());
        response.setEncargadoNombre(bodega.getEncargado().getNombre()
                + " " + bodega.getEncargado().getApellido());
        return response;
    }
}