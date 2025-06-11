package com.clinicaoftalmologica.clinicaoftalmologicabackend.repository;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.MedicamentoTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentoTratamientoRepository extends JpaRepository<MedicamentoTratamiento, Long> {
    List<MedicamentoTratamiento> findByTratamientoId(Long tratamientoId);
    List<MedicamentoTratamiento> findByMedicamentoId(Long medicamentoId);
    void deleteByTratamientoId(Long tratamientoId);
}
