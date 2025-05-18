package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import java.time.LocalDateTime;

public class AntecedenteResponseDTO {
    private Long id;
    private String tipo;
    private String descripcion;
    private LocalDateTime fechaRegistro;

    public AntecedenteResponseDTO() {}

    public AntecedenteResponseDTO(Long id, String tipo, String descripcion, LocalDateTime fechaRegistro) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}