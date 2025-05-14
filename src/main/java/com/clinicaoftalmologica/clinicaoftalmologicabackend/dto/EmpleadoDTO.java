package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cargo;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Departamento;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Especialidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmpleadoDTO {

    private Long id;
    private Usuario usuario;
    private Cargo cargo;
    private Especialidad especialidad;
    private Departamento departamento;
    private String fechaContratacion;
    private Double salario;

    public EmpleadoDTO(Empleado e) {
        this.id = e.getId();
        this.usuario = e.getUsuario();
        this.cargo = e.getCargo();
        this.especialidad = e.getEspecialidad();
        this.departamento = e.getDepartamento();


        LocalDate fecha = e.getFechaContratacion();
        this.fechaContratacion = (fecha != null)
                ? fecha.format(DateTimeFormatter.ISO_DATE)
                : null;


        BigDecimal salarioBD = e.getSalario();
        this.salario = (salarioBD != null) ? salarioBD.doubleValue() : null;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Cargo getCargo() { return cargo; }
    public Especialidad getEspecialidad() { return especialidad; }
    public Departamento getDepartamento() { return departamento; }
    public String getFechaContratacion() { return fechaContratacion; }
    public Double getSalario() { return salario; }
}
