package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Triaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TriajeRepository extends JpaRepository<Triaje, Long> {
    Optional<Triaje> findByCitaId(Long citaId);
}