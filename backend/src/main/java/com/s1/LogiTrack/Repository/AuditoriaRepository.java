package com.s1.LogiTrack.Repository;

import com.s1.LogiTrack.Model.Auditoria;
import com.s1.LogiTrack.Model.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

    List<Auditoria> findByOperacion(TipoOperacion operacion);
    List<Auditoria> findByUsuarioId(Integer usuarioId);
}