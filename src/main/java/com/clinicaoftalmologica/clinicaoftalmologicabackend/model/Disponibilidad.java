// src/main/java/com/clinicaoftalmologica/clinicaoftalmologicabackend/model/Disponibilidad.java
package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad",
        uniqueConstraints = @UniqueConstraint(name = "uq_empleado_fecha",
                columnNames = {"empleado_id", "fecha"}))
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Integer cupos;

    @Column(nullable = false)
    private Integer duracionSlot;  // en minutos

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    public Disponibilidad() { }


    public Disponibilidad(Empleado empleado,
                          LocalDate fecha,
                          Integer cupos,
                          Integer duracionSlot) {
        this.empleado    = empleado;
        this.fecha       = fecha;
        this.duracionSlot = duracionSlot;

        DayOfWeek dia = fecha.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY) {
            this.horaInicio = LocalTime.of(9, 0);
            this.horaFin    = LocalTime.of(14, 0);
            this.cupos      = Math.min(cupos,
                    (int)((horaFin.toSecondOfDay() - horaInicio.toSecondOfDay()) / 60 / duracionSlot));
        }
        else if (dia == DayOfWeek.SUNDAY) {

            this.horaInicio = LocalTime.of(0, 0);
            this.horaFin    = LocalTime.of(0, 0);
            this.cupos      = 0;
        }
        else {

            this.horaInicio = LocalTime.of(7, 0);   // ← cambio: antes era 8:00
            this.horaFin    = LocalTime.of(16, 0);
            this.cupos      = Math.min(cupos,
                    (int)((horaFin.toSecondOfDay() - horaInicio.toSecondOfDay()) / 60 / duracionSlot));
        }
    }
//getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getDuracionSlot() {
        return duracionSlot;
    }

    public void setDuracionSlot(Integer duracionSlot) {
        this.duracionSlot = duracionSlot;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}
