package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class DepartamentoCreateDTO {
    private String nombre;
    private String descripcion;

    public DepartamentoCreateDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}