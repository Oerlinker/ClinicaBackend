package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;

import java.time.LocalDate;

public class AtencionDetailDTO {
    private Long id;
    private LocalDate fecha;
    private String motivo;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private String patologia;


    private Long pacienteId;
    private String pacienteNombre;


    private Long doctorId;
    private String doctorNombre;

    public AtencionDetailDTO(Atencion atencion) {
        this.id = atencion.getId();
        this.fecha = atencion.getFecha();
        this.motivo = atencion.getMotivo();
        this.diagnostico = atencion.getDiagnostico();
        this.tratamiento = atencion.getTratamiento();
        this.observaciones = atencion.getObservaciones();

        if (atencion.getPatologia() != null) {
            this.patologia = atencion.getPatologia().getNombre();
        }

        if (atencion.getCita().getPaciente() != null) {
            this.pacienteId = atencion.getCita().getPaciente().getId();
            this.pacienteNombre = atencion.getCita().getPaciente().getNombre() + " " +
                               atencion.getCita().getPaciente().getApellido();
        }

        if (atencion.getCita().getDoctor() != null) {
            this.doctorId = atencion.getCita().getDoctor().getId();
            this.doctorNombre = atencion.getCita().getDoctor().getUsuario().getNombre() + " " +
                             atencion.getCita().getDoctor().getUsuario().getApellido();
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getPatologia() {
        return patologia;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorNombre() {
        return doctorNombre;
    }
}
