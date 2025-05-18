package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CitaReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.DisponibilidadRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.DisponibilidadService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.specification.CitaSpecification;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.specification.DisponibilidadSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
    private DisponibilidadRepository dispRepo;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/citas")
    public ResponseEntity<List<Cita>> reportCitas(@RequestBody CitaReportFilter filter) {
        Specification<Cita> spec = CitaSpecification.withFilters(filter);
        List<Cita> citas = citaRepo.findAll(spec, Sort.by("fecha").ascending());
        return ResponseEntity.ok(citas);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/disponibilidades")
    public ResponseEntity<List<DisponibilidadDTO>> reportDisponibilidades(
            @RequestBody DisponibilidadReportFilter filter) {

        List<Disponibilidad> disponibilidades = dispRepo.findAll(
                DisponibilidadSpecification.withFilters(filter),
                Sort.by("fecha").ascending()
        );

        List<DisponibilidadDTO> resultado = disponibilidades.stream()
                .map(d -> disponibilidadService.getDisponibilidadWithSlots(
                        d.getEmpleado().getId(), d.getFecha()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

}
