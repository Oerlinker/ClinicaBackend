package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
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
}
