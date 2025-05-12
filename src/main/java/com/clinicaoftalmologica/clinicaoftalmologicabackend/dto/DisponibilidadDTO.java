package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO que representa una disponibilidad de un empleado en una fecha,
 * junto con sus franjas horarias y cupos restantes por slot.
 */
public class DisponibilidadDTO {

    private Long id;
    private EmpleadoSimpleDTO empleado;
    private LocalDate fecha;
    private int cupos;
    private int duracionSlot;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private List<DisponibilidadSlotDTO> slots;

    public DisponibilidadDTO() { }

    public DisponibilidadDTO(
            Long id,
            EmpleadoSimpleDTO empleado,
            LocalDate fecha,
            int cupos,
            int duracionSlot,
            LocalTime horaInicio,
            LocalTime horaFin,
            List<DisponibilidadSlotDTO> slots
    ) {
        this.id = id;
        this.empleado = empleado;
        this.fecha = fecha;
        this.cupos = cupos;
        this.duracionSlot = duracionSlot;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.slots = slots;
    }

    // ==== Getters y setters ====

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

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public int getDuracionSlot() {
        return duracionSlot;
    }

    public void setDuracionSlot(int duracionSlot) {
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

    public List<DisponibilidadSlotDTO> getSlots() {
        return slots;
    }

    public void setSlots(List<DisponibilidadSlotDTO> slots) {
        this.slots = slots;
    }

    // ==== DTO anidado para datos básicos de empleado ====

    public static class EmpleadoSimpleDTO {
        private Long id;
        private String nombreCompleto;

        public EmpleadoSimpleDTO() { }

        public EmpleadoSimpleDTO(Long id, String nombreCompleto) {
            this.id = id;
            this.nombreCompleto = nombreCompleto;
        }

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
