package com.s1.LogiTrack.Service.impl;

import com.s1.LogiTrack.Dto.Request.MovimientoDetalleRequest;
import com.s1.LogiTrack.Dto.Request.MovimientoRequest;
import com.s1.LogiTrack.Dto.Response.MovimientoDetalleResponse;
import com.s1.LogiTrack.Dto.Response.MovimientoResponse;
import com.s1.LogiTrack.Model.*;
import com.s1.LogiTrack.Repository.*;
import com.s1.LogiTrack.Security.SecurityUtils;
import com.s1.LogiTrack.Service.AuditoriaService;
import com.s1.LogiTrack.Service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final MovimientoDetalleRepository movimientoDetalleRepository;
    private final ProductoRepository productoRepository;
    private final PersonaRepository personaRepository;
    private final BodegaRepository bodegaRepository;
    private final AuditoriaService auditoriaService;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public MovimientoResponse crear(MovimientoRequest request, Integer usuarioId) {
        Persona usuario = personaRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Bodega bodegaOrigen = request.getBodegaOrigenId() != null
                ? bodegaRepository.findById(request.getBodegaOrigenId())
                .orElseThrow(() -> new RuntimeException("Bodega origen no encontrada"))
                : null;

        Bodega bodegaDestino = request.getBodegaDestinoId() != null
                ? bodegaRepository.findById(request.getBodegaDestinoId())
                .orElseThrow(() -> new RuntimeException("Bodega destino no encontrada"))
                : null;

        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setDescripcion(request.getDescripcion());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setUsuario(usuario);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);
        movimientoRepository.save(movimiento);

        StringBuilder productosAfectados = new StringBuilder();

        for (MovimientoDetalleRequest detalleRequest : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (request.getTipoMovimiento() == TipoMovimiento.ENTRADA) {
                producto.setStock(producto.getStock() + detalleRequest.getCantidad());
            } else {
                if (producto.getStock() < detalleRequest.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
                }
                producto.setStock(producto.getStock() - detalleRequest.getCantidad());
            }
            productoRepository.save(producto);
            productosAfectados.append(producto.getNombre())
                    .append(" (").append(detalleRequest.getCantidad()).append("), ");

            MovimientoDetalle detalle = new MovimientoDetalle();
            detalle.setMovimiento(movimiento);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleRequest.getCantidad());
            movimientoDetalleRepository.save(detalle);
        }

        auditoriaService.registrar(
                TipoOperacion.CREAR,
                "Movimiento",
                "Movimiento " + request.getTipoMovimiento() + " — Productos: " + productosAfectados,
                usuarioId
        );

        return toResponse(movimiento);
    }

    @Override
    public MovimientoResponse obtenerPorId(Integer id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
        return toResponse(movimiento);
    }

    @Override
    public List<MovimientoResponse> obtenerTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoResponse> obtenerPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MovimientoResponse toResponse(Movimiento movimiento) {
        MovimientoResponse response = new MovimientoResponse();
        response.setId(movimiento.getId());
        response.setTipoMovimiento(movimiento.getTipoMovimiento());
        response.setDescripcion(movimiento.getDescripcion());
        response.setFecha(movimiento.getFecha());
        response.setUsuarioNombre(movimiento.getUsuario().getNombre());
        response.setBodegaOrigenNombre(movimiento.getBodegaOrigen() != null
                ? movimiento.getBodegaOrigen().getNombre() : null);
        response.setBodegaDestinoNombre(movimiento.getBodegaDestino() != null
                ? movimiento.getBodegaDestino().getNombre() : null);
        response.setDetalles(movimientoDetalleRepository
                .findByMovimientoId(movimiento.getId())
                .stream()
                .map(this::toDetalleResponse)
                .collect(Collectors.toList()));
        return response;
    }

    private MovimientoDetalleResponse toDetalleResponse(MovimientoDetalle detalle) {
        MovimientoDetalleResponse response = new MovimientoDetalleResponse();
        response.setId(detalle.getId());
        response.setProductoId(detalle.getProducto().getId());
        response.setProductoNombre(detalle.getProducto().getNombre());
        response.setCantidad(detalle.getCantidad());
        return response;
    }
}