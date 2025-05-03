package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CitaReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.DisponibilidadRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.specification.CitaSpecification;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.specification.DisponibilidadSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
    private DisponibilidadRepository dispRepo;

    @PostMapping("/citas")
    public ResponseEntity<List<Cita>> reportCitas(@RequestBody CitaReportFilter filter) {
        Specification<Cita> spec = CitaSpecification.withFilters(filter);
        List<Cita> citas = citaRepo.findAll(spec, Sort.by("fecha").ascending());
        return ResponseEntity.ok(citas);
    }

    @PostMapping("/disponibilidades")
    public ResponseEntity<List<Disponibilidad>> reportDisponibilidades(
            @RequestBody DisponibilidadReportFilter filter) {

        Specification<Disponibilidad> spec =
                DisponibilidadSpecification.withFilters(filter);

        List<Disponibilidad> rows = dispRepo.findAll(
                spec,
                Sort.by("fecha").ascending()
        );
        return ResponseEntity.ok(rows);
    }
}
