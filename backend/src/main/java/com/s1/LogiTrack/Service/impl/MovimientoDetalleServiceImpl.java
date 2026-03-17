package com.s1.LogiTrack.Service.impl;

import com.s1.LogiTrack.Dto.Response.MovimientoDetalleResponse;
import com.s1.LogiTrack.Repository.MovimientoDetalleRepository;
import com.s1.LogiTrack.Service.MovimientoDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoDetalleServiceImpl implements MovimientoDetalleService {

    private final MovimientoDetalleRepository movimientoDetalleRepository;

    @Override
    public List<MovimientoDetalleResponse> obtenerPorMovimiento(Integer movimientoId) {
        return movimientoDetalleRepository.findByMovimientoId(movimientoId)
                .stream()
                .map(detalle -> {
                    MovimientoDetalleResponse response = new MovimientoDetalleResponse();
                    response.setId(detalle.getId());
                    response.setProductoId(detalle.getProducto().getId());
                    response.setProductoNombre(detalle.getProducto().getNombre());
                    response.setCantidad(detalle.getCantidad());
                    return response;
                })
                .collect(Collectors.toList());
    }
}