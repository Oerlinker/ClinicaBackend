package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class MedicamentoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String fabricante;
    private String efectosSecundarios;
    private Boolean activo;

    // Constructores
    public MedicamentoDTO() {
    }

    public MedicamentoDTO(Long id, String nombre, String descripcion, String fabricante, String efectosSecundarios, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fabricante = fabricante;
        this.efectosSecundarios = efectosSecundarios;
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

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getEfectosSecundarios() {
        return efectosSecundarios;
    }

    public void setEfectosSecundarios(String efectosSecundarios) {
        this.efectosSecundarios = efectosSecundarios;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
