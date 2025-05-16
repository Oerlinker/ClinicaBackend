package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import java.time.LocalDate;

public class CitaDTO {
    private Long id;
    private LocalDate fecha;
    private String hora;
    private String estado;
    private String tipo;
    private Long precio;


    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;


    private Long doctorId;
    private String doctorNombre;
    private String doctorApellido;

    public CitaDTO(Cita cita) {
        this.id = cita.getId();
        this.fecha = cita.getFecha();
        this.hora = cita.getHora() != null ? cita.getHora().toLocalTime().toString() : null;
        this.estado = cita.getEstado().name();
        this.tipo = cita.getTipo();
        this.precio = cita.getPrecio();

        if (cita.getPaciente() != null) {
            this.pacienteId = cita.getPaciente().getId();
            this.pacienteNombre = cita.getPaciente().getNombre();
            this.pacienteApellido = cita.getPaciente().getApellido();
        }

        if (cita.getDoctor() != null) {
            this.doctorId = cita.getDoctor().getId();
            if (cita.getDoctor().getUsuario() != null) {
                this.doctorNombre = cita.getDoctor().getUsuario().getNombre();
                this.doctorApellido = cita.getDoctor().getUsuario().getApellido();
            }
        }
    }


    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getPrecio() {
        return precio;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public String getPacienteApellido() {
        return pacienteApellido;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorNombre() {
        return doctorNombre;
    }

    public String getDoctorApellido() {
        return doctorApellido;
    }
}