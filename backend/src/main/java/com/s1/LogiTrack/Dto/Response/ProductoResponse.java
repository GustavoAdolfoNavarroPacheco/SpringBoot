package com.s1.LogiTrack.Dto.Response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoResponse {

    private Integer id;
    private String nombre;
    private String categoria;
    private Integer stock;
    private BigDecimal precio;
    private Integer bodegaId;
    private String bodegaNombre;
}