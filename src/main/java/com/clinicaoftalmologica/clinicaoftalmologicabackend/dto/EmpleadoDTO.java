package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;

public class EmpleadoDTO {

    private Long id;
    private String nombre;
    private String apellido;

    public EmpleadoDTO() {}

    public EmpleadoDTO(Empleado e) {
        this.id = e.getId();
        this.nombre = e.getUsuario().getNombre();
        this.apellido = e.getUsuario().getApellido();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
}
