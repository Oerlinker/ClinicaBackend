package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.*;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {
    private static final Logger logger = LoggerFactory.getLogger(CitaService.class);

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private TriajeRepository triajeRepo;


   @Transactional
   @Loggable("CREAR_CITA")
   public Cita createCita(Cita cita, Long doctorId, Long pacienteId) throws Exception {
       logger.info("Creando cita con doctorId={}, pacienteId={}, fecha={}, hora={}"
               , doctorId, pacienteId, cita.getFecha(), cita.getHora());

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
           throw new Exception("La fecha de la cita no puede ser anterior a la actual");
       }

       LocalDateTime ahora = LocalDateTime.now();
       if (cita.getFecha().isEqual(LocalDate.now()) && cita.getHora().toLocalTime().isBefore(ahora.toLocalTime())) {
           throw new Exception("La hora de la cita no puede ser anterior a la hora actual para citas de hoy");
       }


       Disponibilidad disp = disponibilidadService
               .obtenerPorEmpleadoYFecha(doctorId, cita.getFecha())
               .orElseThrow(() -> new Exception("No hay disponibilidad configurada..."));


       LocalTime horaCita = cita.getHora().toLocalTime();
       if (horaCita.isBefore(disp.getHoraInicio()) ||
               horaCita.isAfter(disp.getHoraFin().minusMinutes(disp.getDuracionSlot()))) {
           throw new Exception("La hora de la cita (" + horaCita + ") no está dentro del rango disponible...");
       }


      List<Cita> citasExistentes = citaRepository.findByDoctorAndFecha(doctorId, cita.getFecha());
      boolean horaOcupada = citasExistentes.stream()
              .anyMatch(c -> c.getHora().toLocalTime().equals(horaCita));

      if (horaOcupada) {
          throw new Exception("La hora " + horaCita + " ya está ocupada para el doctor en la fecha seleccionada");
      }

       cita.setDoctor(doctor);
       cita.setPaciente(paciente);

       Cita nueva = citaRepository.save(cita);
       logger.info("Cita creada con ID: {}", nueva.getId());
       return nueva;
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
    public List<Cita> getCitasByDoctorId(Long empleadoId) {
        return citaRepository.findByDoctorId(empleadoId);
    }

    @Loggable("ELIMINAR_CITA")
    @Transactional
    public void deleteCita(Long id) throws Exception {
        logger.info("Intentando eliminar cita con ID: {}", id);

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new Exception("Cita no encontrada con ID: " + id));


        paymentRepository.findByCitaId(id).ifPresent(payment -> {
            logger.info("Eliminando pago asociado con ID: {}", payment.getId());
            paymentRepository.delete(payment);
        });


        citaRepository.delete(cita);
        logger.info("Cita con ID: {} eliminada correctamente", id);
    }

    public List<Cita> getCitasByDoctorAndFecha(Long doctorId, LocalDate fecha) {
        return citaRepository.findByDoctorAndFecha(doctorId, fecha);
    }

    @Transactional(readOnly = true)
    public List<Cita> getCitasPendientesTriaje() {

        List<Long> conTriaje = triajeRepo.findAll()
                .stream()
                .map(t -> t.getCita().getId())
                .toList();

        return citaRepository.findAll()
                .stream()
                .filter(c -> !conTriaje.contains(c.getId()))
                .toList();
    }
}



