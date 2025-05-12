package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.DisponibilidadRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class DisponibilidadInitService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private RolRepository rolRepository;

    @Transactional
    public void crearDisponibilidadesParaDoctor(Long doctorId, LocalDate fechaInicio, LocalDate fechaFin) {
        Empleado doctor = empleadoRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado con ID: " + doctorId));


        if (!doctor.getUsuario().getRol().getNombre().equals("DOCTOR")) {
            throw new IllegalArgumentException("El empleado no es un doctor");
        }

        LocalDate fecha = fechaInicio;
        while (!fecha.isAfter(fechaFin)) {

            if (disponibilidadRepository.findByEmpleadoIdAndFecha(doctorId, fecha).isEmpty()) {

                DayOfWeek dia = fecha.getDayOfWeek();
                if (dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY) {
                    Disponibilidad disp = new Disponibilidad(doctor, fecha);
                    disponibilidadRepository.save(disp);
                }
            }
            fecha = fecha.plusDays(1);
        }
    }
}