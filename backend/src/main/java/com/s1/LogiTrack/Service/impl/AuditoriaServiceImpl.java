package com.s1.LogiTrack.Service.impl;

import com.s1.LogiTrack.Dto.Response.AuditoriaResponse;
import com.s1.LogiTrack.Model.Auditoria;
import com.s1.LogiTrack.Model.Persona;
import com.s1.LogiTrack.Model.TipoOperacion;
import com.s1.LogiTrack.Repository.AuditoriaRepository;
import com.s1.LogiTrack.Repository.PersonaRepository;
import com.s1.LogiTrack.Service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final PersonaRepository personaRepository;

    @Override
    public void registrar(TipoOperacion operacion, String entidad, String descripcion, Integer usuarioId) {
        Persona usuario = personaRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Auditoria auditoria = new Auditoria();
        auditoria.setOperacion(operacion);
        auditoria.setEntidad(entidad);
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setUsuario(usuario);
        auditoria.setValorAnterior(null);
        auditoria.setValorNuevo(descripcion);
        auditoriaRepository.save(auditoria);
    }

    @Override
    public List<AuditoriaResponse> obtenerTodos() {
        return auditoriaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditoriaResponse> obtenerPorOperacion(TipoOperacion operacion) {
        return auditoriaRepository.findByOperacion(operacion)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditoriaResponse> obtenerPorUsuario(Integer usuarioId) {
        return auditoriaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AuditoriaResponse toResponse(Auditoria auditoria) {
        AuditoriaResponse response = new AuditoriaResponse();
        response.setId(auditoria.getId());
        response.setEntidad(auditoria.getEntidad());
        response.setOperacion(auditoria.getOperacion());
        response.setFecha(auditoria.getFecha());
        response.setUsuarioId(auditoria.getUsuario().getId());
        response.setUsuarioNombre(auditoria.getUsuario().getNombre());
        response.setValorAnterior(auditoria.getValorAnterior());
        response.setValorNuevo(auditoria.getValorNuevo());
        return response;
    }
}