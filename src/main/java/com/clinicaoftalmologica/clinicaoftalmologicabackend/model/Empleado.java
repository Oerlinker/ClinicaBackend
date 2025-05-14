package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @ManyToOne(optional = true)
    @JoinColumn(name = "especialidad_id", nullable = true)
    private Especialidad especialidad;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"empleado", "rol"})
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = true)
    private Departamento departamento;

    public Empleado() {
    }

    public Empleado(Cargo cargo,
                    Especialidad especialidad,
                    LocalDate fechaContratacion,
                    BigDecimal salario,
                    Usuario usuario) {
        this.cargo = cargo;
        this.especialidad = especialidad;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.usuario = usuario;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Transient
    public Long getEspecialidadId() {
        return especialidad != null ? especialidad.getId() : null;
    }

    @JsonProperty("especialidadId")
    public void setEspecialidadId(Long id) {
        if (id != null) {
            Especialidad e = new Especialidad();
            e.setId(id);
            this.especialidad = e;
        } else {
            this.especialidad = null;
        }
    }
}
