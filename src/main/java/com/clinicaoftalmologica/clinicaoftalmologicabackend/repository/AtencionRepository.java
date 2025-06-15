package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    Optional<Atencion> findByCita_Id(Long citaId);
    List<Atencion> findByCitaPacienteId(Long pacienteId);
    List<Atencion> findByCitaDoctorId(Long doctorId);
    List<Atencion> findByCitaPacienteIdAndCitaDoctorId(Long pacienteId, Long doctorId);
}
