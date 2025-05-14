package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "departamento")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(length = 500)
    private String descripcion;


    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Empleado> empleados;

    public Departamento() {}

    public Departamento(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    //  getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }
}