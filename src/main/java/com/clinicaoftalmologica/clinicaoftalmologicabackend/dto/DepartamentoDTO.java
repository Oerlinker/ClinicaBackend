package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Departamento;

public class DepartamentoDTO {
    private Long id;
    private String nombre;
    private String descripcion;

    public DepartamentoDTO() {}


    public DepartamentoDTO(Departamento dept) {
        this.id          = dept.getId();
        this.nombre      = dept.getNombre();
        this.descripcion = dept.getDescripcion();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
}