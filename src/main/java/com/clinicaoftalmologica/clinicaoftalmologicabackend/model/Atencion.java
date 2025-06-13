package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "cita_id", referencedColumnName = "id")
    private Cita cita;

    private LocalDate fecha;

    private String motivo;
    private String diagnostico;

    // Mantenemos el campo tratamiento para compatibilidad, pero será depreciado
    @Column(length = 1000)
    private String tratamiento;

    // Nueva relación con la entidad Tratamiento
    @OneToMany(mappedBy = "atencion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tratamiento> tratamientos = new HashSet<>();

    @Column(length = 1000)
    private String observaciones;

    @ManyToOne(optional = true)
    @JoinColumn(name = "patologia_id")
    private Patologia patologia;

    // Métodos helper para manejar la relación con tratamientos
    public void addTratamiento(Tratamiento tratamiento) {
        tratamientos.add(tratamiento);
        tratamiento.setAtencion(this);
    }

    public void removeTratamiento(Tratamiento tratamiento) {
        tratamientos.remove(tratamiento);
        tratamiento.setAtencion(null);
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
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

    public Set<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(Set<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Patologia getPatologia() {
        return patologia;
    }

    public void setPatologia(Patologia patologia) {
        this.patologia = patologia;
    }
}
