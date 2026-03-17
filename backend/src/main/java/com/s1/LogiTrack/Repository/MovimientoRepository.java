package com.s1.LogiTrack.Repository;

import com.s1.LogiTrack.Model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    List<Movimiento> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}