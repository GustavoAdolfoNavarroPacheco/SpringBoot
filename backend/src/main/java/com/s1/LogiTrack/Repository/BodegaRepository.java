package com.s1.LogiTrack.Repository;

import com.s1.LogiTrack.Model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Integer> {
}