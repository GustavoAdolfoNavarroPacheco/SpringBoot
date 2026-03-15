package com.s1.gestion_profesion.controller;

import com.s1.gestion_profesion.dto.request.PersonaRequestDTO;
import com.s1.gestion_profesion.dto.response.PersonaResponseDTO;
import com.s1.gestion_profesion.service.impl.PersonaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
@Validated
public class PersonaController {
    private final PersonaServiceImpl personaService;

    @PostMapping
    public ResponseEntity<PersonaResponseDTO> guardar(@Valid @RequestBody PersonaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.guardarPersona(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO> actualizar(@Valid @RequestBody PersonaRequestDTO dto, @PathVariable Long id){
        return ResponseEntity.ok().body(personaService.actualizarPersona(dto, id));
    }

    @GetMapping("/public")
    public ResponseEntity<List<PersonaResponseDTO>>  listarTodos(){
        return ResponseEntity.ok().body(personaService.listarPersonas());
    }
    @Operation(summary = "Lista las edades mayores que",
            description = "Se espera poder filtrar quienes cumplen con la mayoria de " +
                    "cierta edad")
    @GetMapping("/edad")
    public ResponseEntity<List<PersonaResponseDTO>>  listarPorEdad(
            @Parameter(description = "Se espera una edad para mostrar cuales " +
                    "personas son mayores de dicho edad",
                    example = "26")
            @RequestParam Integer edad){
        return ResponseEntity.ok().body(personaService.buscarMayorQueEdad(edad));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO>  buscarId(@PathVariable Long id){
        return ResponseEntity.ok().body(personaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        personaService.eliminarPersona(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
