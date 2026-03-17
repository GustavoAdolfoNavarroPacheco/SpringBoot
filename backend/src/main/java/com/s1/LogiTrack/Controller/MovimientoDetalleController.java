package com.s1.LogiTrack.Controller;

import com.s1.LogiTrack.Dto.Response.MovimientoDetalleResponse;
import com.s1.LogiTrack.Service.MovimientoDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos-detalles")
@RequiredArgsConstructor
public class MovimientoDetalleController {

    private final MovimientoDetalleService movimientoDetalleService;

    @GetMapping("/por-movimiento/{movimientoId}")
    public ResponseEntity<List<MovimientoDetalleResponse>> obtenerPorMovimiento(
            @PathVariable Integer movimientoId) {
        return ResponseEntity.ok(movimientoDetalleService.obtenerPorMovimiento(movimientoId));
    }
}