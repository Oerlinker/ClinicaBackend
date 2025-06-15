package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TratamientoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionDias;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long atencionId;
    private Set<MedicamentoTratamientoDTO> medicamentos = new HashSet<>();
    private List<MedicamentoTratamientoDTO> medicamentosTratamiento;
    private Boolean activo;
    private String observaciones;

    // Constructores
    public TratamientoDTO() {
    }

    public TratamientoDTO(Long id, String nombre, String descripcion, Integer duracionDias,
                         LocalDate fechaInicio, LocalDate fechaFin, Long atencionId, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionDias = duracionDias;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.atencionId = atencionId;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(Integer duracionDias) {
        this.duracionDias = duracionDias;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getAtencionId() {
        return atencionId;
    }

    public void setAtencionId(Long atencionId) {
        this.atencionId = atencionId;
    }

    public Set<MedicamentoTratamientoDTO> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(Set<MedicamentoTratamientoDTO> medicamentos) {
        this.medicamentos = medicamentos;
        // Actualizar medicamentosTratamiento cuando se establece medicamentos
        this.medicamentosTratamiento = medicamentos != null ?
            new ArrayList<>(medicamentos) : new ArrayList<>();
    }

    // Nuevos getters y setters para medicamentosTratamiento
    public List<MedicamentoTratamientoDTO> getMedicamentosTratamiento() {
        // Si medicamentosTratamiento es null pero medicamentos no, convertirlo
        if (medicamentosTratamiento == null && medicamentos != null) {
            medicamentosTratamiento = new ArrayList<>(medicamentos);
        }
        return medicamentosTratamiento != null ? medicamentosTratamiento : new ArrayList<>();
    }

    public void setMedicamentosTratamiento(List<MedicamentoTratamientoDTO> medicamentosTratamiento) {
        this.medicamentosTratamiento = medicamentosTratamiento;
        // Actualizar también medicamentos para mantener sincronía
        if (medicamentosTratamiento != null) {
            this.medicamentos = new HashSet<>(medicamentosTratamiento);
        }
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
