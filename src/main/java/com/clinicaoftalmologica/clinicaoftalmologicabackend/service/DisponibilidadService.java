package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadSlotDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.DisponibilidadRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    @Autowired
    private DisponibilidadRepository repo;

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Transactional
    public Disponibilidad crear(Disponibilidad d) {
        Long empId = d.getEmpleado().getId();
        d.setEmpleado(empleadoRepo.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado")));
        Optional<Disponibilidad> existente =
                repo.findByEmpleadoIdAndFecha(empId, d.getFecha());
        if (existente.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe disponibilidad para el empleado " + empId +
                            " en la fecha " + d.getFecha()
            );
        }


        long minutosTotales = ChronoUnit.MINUTES.between(d.getHoraInicio(), d.getHoraFin());
        int maxCupos       = (int)(minutosTotales / d.getDuracionSlot());
        d.setCupos(maxCupos);
        return repo.save(d);
    }


    @Transactional
    public Disponibilidad actualizar(Disponibilidad d) {

        return repo.save(d);
    }


    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }


    public Optional<Disponibilidad> obtenerPorEmpleadoYFecha(Long empleadoId, java.time.LocalDate fecha) {
        return repo.findByEmpleadoIdAndFecha(empleadoId, fecha);
    }


    public List<Disponibilidad> obtenerPorEmpleado(Long empleadoId) {
        return repo.findByEmpleadoId(empleadoId);
    }


   public DisponibilidadDTO getDisponibilidadWithSlots(Long empleadoId, LocalDate fecha) {
       Disponibilidad disp = repo.findByEmpleadoIdAndFecha(empleadoId, fecha)
               .orElseThrow(() -> new EntityNotFoundException(
                       "Sin disponibilidad para empleado " + empleadoId + " en " + fecha
               ));

       List<LocalTime> all = new ArrayList<>();
       LocalTime t = disp.getHoraInicio();
       while (t.plusMinutes(disp.getDuracionSlot()).compareTo(disp.getHoraFin()) <= 0) {
           all.add(t);
           t = t.plusMinutes(disp.getDuracionSlot());
       }


       List<Cita> citas = citaRepo.findByDoctorAndFecha(empleadoId, fecha);


       Map<LocalTime, Long> usadas = citas.stream()
               .collect(Collectors.groupingBy(
                       c -> c.getHora().toLocalTime(),
                       Collectors.counting()
               ));

       List<DisponibilidadSlotDTO> slots = all.stream()
               .map(hora -> {

                   int restantes = usadas.containsKey(hora) ? 0 : 1;
                   return new DisponibilidadSlotDTO(
                           hora.toString(),
                           restantes
                   );
               })
               .collect(Collectors.toList());

       return new DisponibilidadDTO(
               disp.getId(),
               new DisponibilidadDTO.EmpleadoSimpleDTO(
                       disp.getEmpleado().getId(),
                       disp.getEmpleado().getUsuario().getNombre() + " " +
                               disp.getEmpleado().getUsuario().getApellido()
               ),
               disp.getFecha(),
               disp.getCupos(),
               disp.getDuracionSlot(),
               disp.getHoraInicio(),
               disp.getHoraFin(),
               slots
       );
   }
}
