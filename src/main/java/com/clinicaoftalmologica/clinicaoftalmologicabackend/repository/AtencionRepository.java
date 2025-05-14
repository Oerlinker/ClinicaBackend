package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Long> {
    Optional<Atencion> findByCita_Id(Long citaId);
}