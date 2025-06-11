package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import java.util.ArrayList;
import java.util.List;

public class AtencionRegisterDTO {
    private Long citaId;

    private String motivo;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    private Long patologiaId;

    // Nuevo campo para permitir la creación de tratamientos al registrar una atención
    private List<TratamientoDTO> tratamientos = new ArrayList<>();

    // Getters y setters
    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
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

    public Long getPatologiaId() {
        return patologiaId;
    }

    public void setPatologiaId(Long patologiaId) {
        this.patologiaId = patologiaId;
    }

    public List<TratamientoDTO> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<TratamientoDTO> tratamientos) {
        this.tratamientos = tratamientos;
    }
}
