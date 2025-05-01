package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository
        extends JpaRepository<Cita, Long>,
        JpaSpecificationExecutor<Cita> {

    boolean existsByDoctorAndFechaAndHora(Empleado doctor,
                                          LocalDate fecha,
                                          LocalDateTime hora);

    @Query("SELECT c FROM Cita c WHERE c.doctor.id = :doctorId AND c.fecha = :fecha")
    List<Cita> findByDoctorAndFecha(@Param("doctorId") Long doctorId,
                                    @Param("fecha") LocalDate fecha);

    List<Cita> findByPacienteId(Long pacienteId);

    List<Cita> findByDoctorId(Long doctorId);
}