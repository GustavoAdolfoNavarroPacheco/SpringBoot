package com.s1.LogiTrack.Controller;

import com.s1.LogiTrack.Dto.Response.AuditoriaResponse;
import com.s1.LogiTrack.Model.TipoOperacion;
import com.s1.LogiTrack.Service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> obtenerTodos() {
        return ResponseEntity.ok(auditoriaService.obtenerTodos());
    }

    @GetMapping("/por-operacion")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorOperacion(
            @RequestParam TipoOperacion operacion) {
        return ResponseEntity.ok(auditoriaService.obtenerPorOperacion(operacion));
    }

    @GetMapping("/por-usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorUsuario(
            @PathVariable Integer usuarioId) {
        return ResponseEntity.ok(auditoriaService.obtenerPorUsuario(usuarioId));
    }
}