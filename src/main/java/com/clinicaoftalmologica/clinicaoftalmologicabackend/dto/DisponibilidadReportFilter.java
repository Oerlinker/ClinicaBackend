
package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import java.time.LocalDate;

public class DisponibilidadReportFilter {
    private Long doctorId;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
