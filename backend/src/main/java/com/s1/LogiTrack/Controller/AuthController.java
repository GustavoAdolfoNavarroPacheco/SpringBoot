package com.s1.LogiTrack.Controller;

import com.s1.LogiTrack.Dto.Request.LoginRequest;
import com.s1.LogiTrack.Dto.Request.PersonaRequest;
import com.s1.LogiTrack.Dto.Response.AuthResponse;
import com.s1.LogiTrack.Dto.Response.PersonaResponse;
import com.s1.LogiTrack.Model.Persona;
import com.s1.LogiTrack.Repository.PersonaRepository;
import com.s1.LogiTrack.Security.JwtUtil;
import com.s1.LogiTrack.Service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PersonaRepository personaRepository;
    private final PersonaService personaService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        Persona persona = personaRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generarToken(persona.getEmail(), persona.getRol().name());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setNombre(persona.getNombre());
        response.setApellido(persona.getApellido());
        response.setEmail(persona.getEmail());
        response.setRol(persona.getRol());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<PersonaResponse> registro(@Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.ok(personaService.crear(request));
    }
}