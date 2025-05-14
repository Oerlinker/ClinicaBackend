package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;

public class EmpleadoDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String cargo;
    private String especialidad;
    private String departamento;
    private String fechaContratacion;
    private Double salario;

    public EmpleadoDTO() {}

    public EmpleadoDTO(Empleado e) {
        this.id = e.getId();
        this.nombre = e.getUsuario().getNombre();
        this.apellido = e.getUsuario().getApellido();
        this.cargo = e.getCargo() != null ? e.getCargo().getNombre() : null;
        this.especialidad = e.getEspecialidad() != null ? e.getEspecialidad().getNombre() : null;
        this.departamento = e.getDepartamento() != null ? e.getDepartamento().getNombre() : null;
        this.fechaContratacion = e.getFechaContratacion() != null ? e.getFechaContratacion().toString() : null;
        this.salario = e.getSalario() != null ? e.getSalario().doubleValue() : null;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
}
