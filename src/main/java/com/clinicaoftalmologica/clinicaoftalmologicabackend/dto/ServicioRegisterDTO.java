package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class ServicioRegisterDTO {

    private String nombre;
    private String descripcion;
    private Long precio;

    public ServicioRegisterDTO() {}

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

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }
}