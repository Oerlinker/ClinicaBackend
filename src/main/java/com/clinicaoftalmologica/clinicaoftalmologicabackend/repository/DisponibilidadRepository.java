package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    Optional<Disponibilidad> findByEmpleadoIdAndFecha(Long empleadoId, LocalDate fecha);
    List<Disponibilidad> findByEmpleadoId(Long empleadoId);
}
