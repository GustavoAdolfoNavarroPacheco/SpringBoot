package com.s1.LogiTrack.Controller;

import com.s1.LogiTrack.Dto.Request.MovimientoRequest;
import com.s1.LogiTrack.Dto.Response.MovimientoResponse;
import com.s1.LogiTrack.Model.Persona;
import com.s1.LogiTrack.Repository.PersonaRepository;
import com.s1.LogiTrack.Service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;
    private final PersonaRepository personaRepository;

    @PostMapping
    public ResponseEntity<MovimientoResponse> crear(@Valid @RequestBody MovimientoRequest request,
                                                    Authentication authentication) {
        Persona persona = personaRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(movimientoService.crear(request, persona.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos() {
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

    @GetMapping("/por-fechas")
    public ResponseEntity<List<MovimientoResponse>> obtenerPorFechas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        return ResponseEntity.ok(movimientoService.obtenerPorFechas(inicio, fin));
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<MovimientoResponse>> listarRecientes() {
        return ResponseEntity.ok(movimientoService.listarRecientes());
    }

    @GetMapping("/cantidad")
    public ResponseEntity<List<MovimientoResponse>> cantidad(
            @RequestParam Integer id) {
        return ResponseEntity.ok(movimientoService.cantidad(id));
    }

    @GetMapping("/cantidad")
    public ResponseEntity<List<MovimientoResponse>> cantidadEntrada(
            @RequestParam Integer id) {
        return ResponseEntity.ok(movimientoService.cantidad(id));
    }

    @GetMapping("/cantidad")
    public ResponseEntity<List<MovimientoResponse>> cantidadSalida(
            @RequestParam Integer id) {
        return ResponseEntity.ok(movimientoService.cantidad(id));
    }

    @GetMapping("/cantidad")
    public ResponseEntity<List<MovimientoResponse>> cantidadTransferencia(
            @RequestParam Integer id) {
        return ResponseEntity.ok(movimientoService.cantidad(id));
    }
}