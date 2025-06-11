package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tratamiento")
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private Integer duracionDias;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "atencion_id")
    private Atencion atencion;

    @OneToMany(mappedBy = "tratamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MedicamentoTratamiento> medicamentos = new HashSet<>();

    private Boolean activo = true;

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

    public Integer getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(Integer duracionDias) {
        this.duracionDias = duracionDias;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Atencion getAtencion() {
        return atencion;
    }

    public void setAtencion(Atencion atencion) {
        this.atencion = atencion;
    }

    public Set<MedicamentoTratamiento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(Set<MedicamentoTratamiento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // Helper method to add a medicamento to the tratamiento
    public void addMedicamento(MedicamentoTratamiento medicamentoTratamiento) {
        medicamentos.add(medicamentoTratamiento);
        medicamentoTratamiento.setTratamiento(this);
    }

    // Helper method to remove a medicamento from the tratamiento
    public void removeMedicamento(MedicamentoTratamiento medicamentoTratamiento) {
        medicamentos.remove(medicamentoTratamiento);
        medicamentoTratamiento.setTratamiento(null);
    }
}
