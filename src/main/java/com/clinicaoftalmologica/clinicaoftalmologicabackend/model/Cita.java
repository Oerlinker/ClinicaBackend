package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalDateTime hora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CitaEstado estado;

    @Column
    private String tipo;

    @Column(nullable = false)
    private Long precio;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = true)
    private Usuario paciente;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado doctor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public CitaEstado getEstado() {
        return estado;
    }

    public void setEstado(CitaEstado estado) {

        this.estado = estado;
    }

    public String getTipo() {

        return tipo;
    }

    public void setTipo(String tipo) {

        this.tipo = tipo;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Usuario getPaciente() {

        return paciente;
    }

    public void setPaciente(Usuario paciente) {

        this.paciente = paciente;
    }

    public Empleado getDoctor() {

        return doctor;
    }

    public void setDoctor(Empleado doctor) {

        this.doctor = doctor;
    }
}
