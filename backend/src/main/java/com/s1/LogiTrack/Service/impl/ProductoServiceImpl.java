package com.s1.LogiTrack.Service.impl;

import com.s1.LogiTrack.Dto.Request.ProductoRequest;
import com.s1.LogiTrack.Dto.Response.ProductoResponse;
import com.s1.LogiTrack.Model.*;
import com.s1.LogiTrack.Repository.BodegaRepository;
import com.s1.LogiTrack.Repository.MovimientoDetalleRepository;
import com.s1.LogiTrack.Repository.ProductoRepository;
import com.s1.LogiTrack.Security.SecurityUtils;
import com.s1.LogiTrack.Service.AuditoriaService;
import com.s1.LogiTrack.Service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final AuditoriaService auditoriaService;
    private final SecurityUtils securityUtils;
    private final MovimientoDetalleRepository movimientoDetalleRepository;

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        Bodega bodega = bodegaRepository.findById(request.getBodegaId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setCategoria(request.getCategoria());
        producto.setStock(request.getStock());
        producto.setPrecio(request.getPrecio());
        producto.setBodega(bodega);
        Producto saved = productoRepository.save(producto);

        Persona usuario = securityUtils.getUsuarioActual();
        auditoriaService.registrar(
                TipoOperacion.CREAR,
                "Producto",
                "Se creó el producto: " + saved.getNombre(),
                usuario.getId()
        );

        return toResponse(saved);
    }

    @Override
    public ProductoResponse obtenerPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toResponse(producto);
    }

    @Override
    public List<ProductoResponse> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoResponse actualizar(Integer id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Bodega bodega = bodegaRepository.findById(request.getBodegaId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        producto.setNombre(request.getNombre());
        producto.setCategoria(request.getCategoria());
        producto.setStock(request.getStock());
        producto.setPrecio(request.getPrecio());
        producto.setBodega(bodega);
        Producto saved = productoRepository.save(producto);

        Persona usuario = securityUtils.getUsuarioActual();
        auditoriaService.registrar(
                TipoOperacion.ACTUALIZAR,
                "Producto",
                "Se actualizó el producto: " + saved.getNombre(),
                usuario.getId()
        );

        return toResponse(saved);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Eliminar detalles de movimiento asociados primero
        movimientoDetalleRepository.deleteByProductoId(id);

        Persona usuario = securityUtils.getUsuarioActual();
        auditoriaService.registrar(
                TipoOperacion.ELIMINAR,
                "Producto",
                "Se eliminó el producto: " + producto.getNombre(),
                usuario.getId()
        );

        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoResponse> obtenerConStockBajo(Integer limite) {
        return productoRepository.findByStockLessThan(limite)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProductoResponse toResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setCategoria(producto.getCategoria());
        response.setStock(producto.getStock());
        response.setPrecio(producto.getPrecio());
        response.setBodegaId(producto.getBodega().getId());
        response.setBodegaNombre(producto.getBodega().getNombre());
        return response;
    }
}