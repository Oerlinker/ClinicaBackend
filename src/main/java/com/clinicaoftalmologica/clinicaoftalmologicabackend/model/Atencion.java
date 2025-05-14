package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Empleado doctor;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;

    private LocalDate fecha;

    private String motivo;
    private String diagnostico;
    private String tratamiento;

    @Column(length = 1000)
    private String observaciones;

    // Getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Empleado getDoctor() {
        return doctor;
    }

    public void setDoctor(Empleado doctor) {
        this.doctor = doctor;
    }

    public Usuario getPaciente() {
        return paciente;
    }

    public void setPaciente(Usuario paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
