package com.s1.LogiTrack.Repository;

import com.s1.LogiTrack.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByStockLessThan(int cantidad);
}