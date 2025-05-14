package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.AtencionRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AtencionService {

    private final AtencionRepository atencionRepo;
    private final CitaRepository citaRepo;
    private final EmpleadoRepository empleadoRepo;
    private final UsuarioRepository usuarioRepo;

    public Atencion registrar(AtencionRegisterDTO dto) {
        Cita cita = citaRepo.findById(dto.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Empleado doctor = empleadoRepo.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        Usuario paciente = usuarioRepo.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Atencion atencion = new Atencion();
        atencion.setCita(cita);
        atencion.setDoctor(doctor);
        atencion.setPaciente(paciente);
        atencion.setFecha(LocalDate.now());
        atencion.setMotivo(dto.getMotivo());
        atencion.setDiagnostico(dto.getDiagnostico());
        atencion.setTratamiento(dto.getTratamiento());
        atencion.setObservaciones(dto.getObservaciones());

        return atencionRepo.save(atencion);
    }
}