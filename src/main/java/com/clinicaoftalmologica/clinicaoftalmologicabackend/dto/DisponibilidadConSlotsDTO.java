
package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DisponibilidadConSlotsDTO {
    private Long id;
    private EmpleadoSimpleDTO empleado;
    private LocalDate fecha;
    private Integer cupos;
    private Integer duracionSlot;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private List<String> slotsDisponibles;


    public DisponibilidadConSlotsDTO(Disponibilidad d, List<String> slots) {
        this.id = d.getId();
        this.empleado = new EmpleadoSimpleDTO(d.getEmpleado());
        this.fecha = d.getFecha();
        this.cupos = d.getCupos();
        this.duracionSlot = d.getDuracionSlot();
        this.horaInicio = d.getHoraInicio();
        this.horaFin = d.getHoraFin();
        this.slotsDisponibles = slots;
    }

    // Getters y Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmpleadoSimpleDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoSimpleDTO empleado) {
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

    public Integer getDuracionSlot() {
        return duracionSlot;
    }

    public void setDuracionSlot(Integer duracionSlot) {
        this.duracionSlot = duracionSlot;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public List<String> getSlotsDisponibles() {
        return slotsDisponibles;
    }

    public void setSlotsDisponibles(List<String> slotsDisponibles) {
        this.slotsDisponibles = slotsDisponibles;
    }


    public static class EmpleadoSimpleDTO {
        private Long id;
        private String nombreCompleto;

        public EmpleadoSimpleDTO(Empleado e) {
            this.id = e.getId();
            if (e.getUsuario() != null) {
                this.nombreCompleto = e.getUsuario().getNombre() + " " + e.getUsuario().getApellido();
            } else {
                this.nombreCompleto = "N/A";
            }
        }

        // Getters y Setters...

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public void setNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }
    }
}