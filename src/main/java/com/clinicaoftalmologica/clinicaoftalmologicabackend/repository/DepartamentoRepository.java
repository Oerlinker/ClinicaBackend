package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository
        extends JpaRepository<Departamento, Long> {
    boolean existsByNombre(String nombre);
}