package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CitaReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.specification.CitaSpecification;
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

    @PostMapping("/citas")
    public ResponseEntity<List<Cita>> reportCitas(@RequestBody CitaReportFilter filter) {
        Specification<Cita> spec = CitaSpecification.withFilters(filter);
        List<Cita> citas = citaRepo.findAll(spec, Sort.by("fecha").ascending());
        return ResponseEntity.ok(citas);
    }
}
