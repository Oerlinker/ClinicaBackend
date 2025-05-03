package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.DisponibilidadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class DisponibilidadService {

    @Autowired
    private DisponibilidadRepository repo;


    @Transactional
    public Disponibilidad crear(Disponibilidad d) {
        Long empId = d.getEmpleado().getId();
        Optional<Disponibilidad> existente =
                repo.findByEmpleadoIdAndFecha(empId, d.getFecha());
        if (existente.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe disponibilidad para el empleado " + empId +
                            " en la fecha " + d.getFecha()
            );
        }


        long minutosTotales = ChronoUnit.MINUTES.between(d.getHoraInicio(), d.getHoraFin());
        int maxCupos = (int)(minutosTotales / d.getDuracionSlot());
        d.setCupos(Math.min(d.getCupos(), maxCupos));

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
}
