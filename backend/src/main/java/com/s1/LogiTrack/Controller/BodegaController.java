package com.s1.LogiTrack.Controller;

import com.s1.LogiTrack.Dto.Request.BodegaRequest;
import com.s1.LogiTrack.Dto.Response.BodegaResponse;
import com.s1.LogiTrack.Service.BodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    @PostMapping
    public ResponseEntity<BodegaResponse> crear(@Valid @RequestBody BodegaRequest request) {
        return ResponseEntity.ok(bodegaService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(bodegaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<BodegaResponse>> obtenerTodos() {
        return ResponseEntity.ok(bodegaService.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponse> actualizar(@PathVariable Integer id,
                                                     @Valid @RequestBody BodegaRequest request) {
        return ResponseEntity.ok(bodegaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        bodegaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}