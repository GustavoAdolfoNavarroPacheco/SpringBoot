package com.s1.LogiTrack.Controller;

import com.s1.LogiTrack.Dto.Request.PersonaRequest;
import com.s1.LogiTrack.Dto.Response.PersonaResponse;
import com.s1.LogiTrack.Service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @PostMapping
    public ResponseEntity<PersonaResponse> crear(@Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.ok(personaService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PersonaResponse>> obtenerTodos() {
        return ResponseEntity.ok(personaService.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponse> actualizar(@PathVariable Integer id,
                                                      @Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.ok(personaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        personaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}