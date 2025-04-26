package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
    List<Bitacora> findByUsuarioId(Long usuarioId);

    List<Bitacora> findAllByOrderByFechaDesc();

    List<Bitacora> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

    List<Bitacora> findByUsuarioIdAndFechaBetween(Long usuarioId,
                                                  LocalDateTime desde,
                                                  LocalDateTime hasta);
}
