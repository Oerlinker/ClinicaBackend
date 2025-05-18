package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class ServicioResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Long precio;

    public ServicioResponseDTO() {}

    public ServicioResponseDTO(Long id, String nombre, String descripcion, Long precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

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

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }
}