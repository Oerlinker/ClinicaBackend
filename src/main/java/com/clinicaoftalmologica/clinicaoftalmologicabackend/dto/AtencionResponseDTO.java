package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;

import java.time.LocalDate;

public class AtencionResponseDTO {
    private Long id;
    private LocalDate fecha;
    private String motivo;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private PatologiaResponseDTO patologia;

    public AtencionResponseDTO(Atencion a) {
        this.id = a.getId();
        this.fecha = a.getFecha();
        this.motivo = a.getMotivo();
        this.diagnostico = a.getDiagnostico();
        this.tratamiento = a.getTratamiento();
        this.observaciones = a.getObservaciones();
        if (a.getPatologia() != null) {
            var p = a.getPatologia();
            this.patologia = new PatologiaResponseDTO(
                    p.getId(),
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion()
            );
        }
    }
    // getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public PatologiaResponseDTO getPatologia() {
        return patologia;
    }

    public void setPatologia(PatologiaResponseDTO patologia) {
        this.patologia = patologia;
    }
}
