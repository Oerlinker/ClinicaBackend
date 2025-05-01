package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.CitaEstado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {
    private static final Logger logger = LoggerFactory.getLogger(CitaService.class);

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Loggable("CREAR_CITA")
    public Cita createCita(Cita cita, Long doctorId, Long pacienteId) throws Exception {
        logger.info("Creando cita con doctorId={}, pacienteId={}, fecha={}, hora={}",
                doctorId, pacienteId, cita.getFecha(), cita.getHora());


        Empleado doctor = empleadoRepository.findById(doctorId)
                .orElseThrow(() -> new Exception("Doctor no encontrado con ID: " + doctorId));


        Usuario paciente = usuarioRepository.findById(pacienteId)
                .orElseThrow(() -> new Exception("Paciente no encontrado con ID: " + pacienteId));


        if (cita.getFecha() == null) {
            throw new Exception("La fecha de la cita es requerida");
        }

        if (cita.getHora() == null) {
            throw new Exception("La hora de la cita es requerida");
        }

        if (cita.getFecha().isBefore(LocalDate.now())) {
            throw new Exception("La fecha de la cita no puede ser anterior a la fecha actual");
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (cita.getHora().isBefore(ahora)) {
            throw new Exception("La hora de la cita no puede ser anterior a la hora actual");
        }


        if (citaRepository.existsByDoctorAndFechaAndHora(doctor, cita.getFecha(), cita.getHora())) {
            throw new Exception("El doctor ya tiene una cita programada en esa fecha y hora");
        }


        cita.setDoctor(doctor);
        cita.setPaciente(paciente);

        logger.info("Guardando cita: {}", cita);
        return citaRepository.save(cita);
    }

    public List<Cita> getAllCitas() {
        return citaRepository.findAll();
    }

    public List<Cita> getCitasByPacienteId(Long pacienteId) {
        logger.info("Buscando citas para el paciente con ID: {}", pacienteId);
        return citaRepository.findByPacienteId(pacienteId);
    }

    @Loggable("PAGAR_CITA")
    @Transactional
    public Cita markCitaPagada(Long citaId) throws Exception {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new Exception("Cita no encontrada con ID: " + citaId));
        cita.setEstado(CitaEstado.PAGADA);
        return citaRepository.save(cita);
    }

    @Loggable("CANCELAR_CITA_PACIENTE")
    @Transactional
    public Cita cancelarCitaPorPaciente(Long citaId, Long pacienteId) throws Exception {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new Exception("Cita no encontrada"));
        if (!cita.getPaciente().getId().equals(pacienteId)) {
            throw new Exception("No puedes cancelar una cita que no es tuya");
        }
        cita.setEstado(CitaEstado.CANCELADA);
        return citaRepository.save(cita);
    }

    @Loggable("CITA_REALIZADA_DOCTOR")
    @Transactional
    public Cita marcarRealizadaPorDoctor(Long citaId, Long doctorId) throws Exception {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new Exception("Cita no encontrada"));
        if (!cita.getDoctor().getId().equals(doctorId)) {
            throw new Exception("No puedes modificar una cita que no es tuya");
        }
        cita.setEstado(CitaEstado.REALIZADA);
        return citaRepository.save(cita);
    }

    @Loggable("CANCELAR_CITA_DOCTOR")
    @Transactional
    public Cita cancelarCitaPorDoctor(Long citaId, Long doctorId) throws Exception {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new Exception("Cita no encontrada"));
        if (!cita.getDoctor().getId().equals(doctorId)) {
            throw new Exception("No puedes cancelar una cita que no es tuya");
        }
        cita.setEstado(CitaEstado.CANCELADA);
        return citaRepository.save(cita);
    }

    @Loggable("BUSCAR_CITAS_POR_DOCTOR")
    public List<Cita> getCitasByDoctorId(Long doctorId) {
        logger.info("Buscando citas para el doctor con ID: {}", doctorId);
        Empleado doctor = empleadoRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el doctor con ID: " + doctorId));
        return citaRepository.findByDoctorId(doctorId);
    }
}
