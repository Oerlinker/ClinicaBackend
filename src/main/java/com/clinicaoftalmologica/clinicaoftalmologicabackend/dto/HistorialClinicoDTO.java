package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import java.util.List;
import java.util.Set;

public class HistorialClinicoDTO {
    private List<AntecedenteResponseDTO> antecedentes;
    private List<AtencionResponseDTO> atenciones;
    private List<Cita> citas;
    private Set<DoctorDTO> medicosVisitados;

    public List<AntecedenteResponseDTO> getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(List<AntecedenteResponseDTO> antecedentes) {
        this.antecedentes = antecedentes;
    }

    public List<AtencionResponseDTO> getAtenciones() {
        return atenciones;
    }

    public void setAtenciones(List<AtencionResponseDTO> atenciones) {
        this.atenciones = atenciones;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public Set<DoctorDTO> getMedicosVisitados() {
        return medicosVisitados;
    }

    public void setMedicosVisitados(Set<DoctorDTO> medicosVisitados) {
        this.medicosVisitados = medicosVisitados;
    }
}