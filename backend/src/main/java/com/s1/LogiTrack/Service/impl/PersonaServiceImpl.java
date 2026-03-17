package com.s1.LogiTrack.Service.impl;

import com.s1.LogiTrack.Dto.Request.PersonaRequest;
import com.s1.LogiTrack.Dto.Response.PersonaResponse;
import com.s1.LogiTrack.Model.Persona;
import com.s1.LogiTrack.Repository.PersonaRepository;
import com.s1.LogiTrack.Service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PersonaResponse crear(PersonaRequest request) {
        Persona persona = new Persona();
        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDocumento(request.getDocumento());
        persona.setEmail(request.getEmail());
        persona.setPassword(passwordEncoder.encode(request.getPassword()));
        persona.setRol(request.getRol());
        return toResponse(personaRepository.save(persona));
    }

    @Override
    public PersonaResponse obtenerPorId(Integer id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        return toResponse(persona);
    }

    @Override
    public List<PersonaResponse> obtenerTodos() {
        return personaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PersonaResponse actualizar(Integer id, PersonaRequest request) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDocumento(request.getDocumento());
        persona.setEmail(request.getEmail());
        persona.setPassword(passwordEncoder.encode(request.getPassword()));
        persona.setRol(request.getRol());
        return toResponse(personaRepository.save(persona));
    }

    @Override
    public void eliminar(Integer id) {
        personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        personaRepository.deleteById(id);
    }

    private PersonaResponse toResponse(Persona persona) {
        PersonaResponse response = new PersonaResponse();
        response.setId(persona.getId());
        response.setNombre(persona.getNombre());
        response.setApellido(persona.getApellido());
        response.setDocumento(persona.getDocumento());
        response.setEmail(persona.getEmail());
        response.setRol(persona.getRol());
        return response;
    }
}