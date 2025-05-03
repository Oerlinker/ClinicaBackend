package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CitaReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadConSlotsDTO;
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

    @PostMapping("/citas")
    public ResponseEntity<List<Cita>> reportCitas(@RequestBody CitaReportFilter filter) {
        Specification<Cita> spec = CitaSpecification.withFilters(filter);
        List<Cita> citas = citaRepo.findAll(spec, Sort.by("fecha").ascending());
        return ResponseEntity.ok(citas);
    }

    @PostMapping("/disponibilidades")
    public ResponseEntity<List<DisponibilidadConSlotsDTO>> reportDisponibilidades(
            @RequestBody DisponibilidadReportFilter filter) {

        Specification<Disponibilidad> spec =
                DisponibilidadSpecification.withFilters(filter);

        List<Disponibilidad> disponibilidades = dispRepo.findAll(
                spec,
                Sort.by("fecha").ascending()
        );

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<DisponibilidadConSlotsDTO> resultado = disponibilidades.stream().map(disp -> {
            List<Cita> citas = citaRepo.findByDoctorAndFecha(
                    disp.getEmpleado().getId(),
                    disp.getFecha()
            );

            Map<String, Long> bookedSlotsCount = citas.stream()
                    .filter(c -> c.getHora() != null)
                    .map(c -> c.getHora().toLocalTime().format(timeFormatter))
                    .collect(Collectors.groupingBy(hora -> hora, Collectors.counting()));

            List<String> allSlots = new ArrayList<>();
            LocalTime currentSlot = disp.getHoraInicio();
            while (!currentSlot.isAfter(disp.getHoraFin().minusMinutes(disp.getDuracionSlot()))) {
                allSlots.add(currentSlot.format(timeFormatter));
                currentSlot = currentSlot.plusMinutes(disp.getDuracionSlot());
            }

            List<String> availableSlots = allSlots.stream()
                    .filter(slot -> {
                        long usedCount = bookedSlotsCount.getOrDefault(slot, 0L);
                        return usedCount < disp.getCupos();
                    })
                    .collect(Collectors.toList());

            return new DisponibilidadConSlotsDTO(disp, availableSlots);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }


}
