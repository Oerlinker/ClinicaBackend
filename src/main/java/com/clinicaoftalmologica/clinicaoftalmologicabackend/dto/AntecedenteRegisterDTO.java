package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class AntecedenteRegisterDTO {
    private Long usuarioId;
    private String tipo;
    private String descripcion;

    public AntecedenteRegisterDTO() {}

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}