package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Tratamiento;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AtencionResponseDTO {
    private Long id;
    private LocalDate fecha;
    private String motivo;
    private String diagnostico;
    private String observaciones;
    private PatologiaResponseDTO patologia;
    // Nuevo campo para incluir los tratamientos
    private List<TratamientoDTO> tratamientos;

    public AtencionResponseDTO(Atencion a) {
        this.id = a.getId();
        this.fecha = a.getFecha();
        this.motivo = a.getMotivo();
        this.diagnostico = a.getDiagnostico();
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

        // Convertir los tratamientos a DTOs
        if (a.getTratamientos() != null && !a.getTratamientos().isEmpty()) {
            this.tratamientos = a.getTratamientos().stream()
                .filter(t -> t.getActivo() != null && t.getActivo())
                .map(this::convertTratamientoToDTO)
                .collect(Collectors.toList());
        }
    }


    private TratamientoDTO convertTratamientoToDTO(Tratamiento tratamiento) {
        TratamientoDTO dto = new TratamientoDTO();
        dto.setId(tratamiento.getId());
        dto.setNombre(tratamiento.getNombre());
        dto.setDescripcion(tratamiento.getDescripcion());
        dto.setDuracionDias(tratamiento.getDuracionDias());
        dto.setFechaInicio(tratamiento.getFechaInicio());
        dto.setFechaFin(tratamiento.getFechaFin());
        dto.setAtencionId(tratamiento.getAtencion() != null ? tratamiento.getAtencion().getId() : null);
        dto.setActivo(tratamiento.getActivo());
        return dto;
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

    public List<TratamientoDTO> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<TratamientoDTO> tratamientos) {
        this.tratamientos = tratamientos;
    }
}
