package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;

import java.time.LocalDate;

public class AtencionDTO {
    private Long id;
    private String pacienteNombre;
    private String doctorNombre;
    private LocalDate fecha;
    private String motivo;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    public AtencionDTO(Atencion a) {
        this.id = a.getId();
        this.pacienteNombre = a.getPaciente() != null ? a.getPaciente().getNombre() + " " + a.getPaciente().getApellido() : null;
        this.doctorNombre = a.getDoctor() != null && a.getDoctor().getUsuario() != null ?
                          a.getDoctor().getUsuario().getNombre() + " " + a.getDoctor().getUsuario().getApellido() : null;
        this.fecha = a.getFecha();
        this.motivo = a.getMotivo();
        this.diagnostico = a.getDiagnostico();
        this.tratamiento = a.getTratamiento();
        this.observaciones = a.getObservaciones();
    }

    // getters

    public Long getId() {
        return id;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public String getDoctorNombre() {
        return doctorNombre;
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
}
