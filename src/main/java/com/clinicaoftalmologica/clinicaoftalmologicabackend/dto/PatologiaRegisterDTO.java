package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;


public class PatologiaRegisterDTO {
    private String codigo;
    private String nombre;
    private String descripcion;

    public PatologiaRegisterDTO() {}

    // Getters y setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
