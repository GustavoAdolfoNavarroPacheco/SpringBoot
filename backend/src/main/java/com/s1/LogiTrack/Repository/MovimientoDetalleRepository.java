package com.s1.LogiTrack.Repository;

import com.s1.LogiTrack.Model.MovimientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoDetalleRepository extends JpaRepository<MovimientoDetalle, Integer> {

    List<MovimientoDetalle> findByMovimientoId(Integer movimientoId);

    void deleteByProductoId(Integer productoId);
}