package com.s1.LogiTrack.Service;

import com.s1.LogiTrack.Dto.Request.ProductoRequest;
import com.s1.LogiTrack.Dto.Response.ProductoResponse;

import java.util.List;

public interface ProductoService {

    ProductoResponse crear(ProductoRequest request);
    ProductoResponse obtenerPorId(Integer id);
    List<ProductoResponse> obtenerTodos();
    ProductoResponse actualizar(Integer id, ProductoRequest request);
    void eliminar(Integer id);
    List<ProductoResponse> obtenerConStockBajo(Integer limite);
}