package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class DoctorDTO {
    private Long usuarioId;
    private Long empleadoId;
    private String nombre;
    private String apellido;

    public DoctorDTO() {}
    public DoctorDTO(Long usuarioId, Long empleadoId, String nombre, String apellido) {
        this.usuarioId = usuarioId;
        this.empleadoId = empleadoId;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}