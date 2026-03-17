package com.s1.LogiTrack.Repository;

import com.s1.LogiTrack.Model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    Optional<Persona> findByEmail(String email);

    Optional<Persona> findByDocumento(String documento);

    boolean existsByEmail(String email);
}