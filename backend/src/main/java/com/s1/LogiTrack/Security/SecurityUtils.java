package com.s1.LogiTrack.Security;

import com.s1.LogiTrack.Model.Persona;
import com.s1.LogiTrack.Repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final PersonaRepository personaRepository;

    public Persona getUsuarioActual() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return personaRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}