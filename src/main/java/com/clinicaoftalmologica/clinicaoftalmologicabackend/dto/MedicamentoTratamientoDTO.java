package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class MedicamentoTratamientoDTO {
    private Long id;
    private Long tratamientoId;
    private Long medicamentoId;
    private String nombreMedicamento; // Para mostrar información del medicamento
    private String dosis;
    private String unidadMedida;
    private String frecuencia;
    private Integer duracionDias;
    private String viaAdministracion;
    private String instrucciones;

    // Constructores
    public MedicamentoTratamientoDTO() {
    }

    public MedicamentoTratamientoDTO(Long id, Long tratamientoId, Long medicamentoId, String nombreMedicamento,
                                   String dosis, String unidadMedida, String frecuencia, Integer duracionDias,
                                   String viaAdministracion, String instrucciones) {
        this.id = id;
        this.tratamientoId = tratamientoId;
        this.medicamentoId = medicamentoId;
        this.nombreMedicamento = nombreMedicamento;
        this.dosis = dosis;
        this.unidadMedida = unidadMedida;
        this.frecuencia = frecuencia;
        this.duracionDias = duracionDias;
        this.viaAdministracion = viaAdministracion;
        this.instrucciones = instrucciones;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTratamientoId() {
        return tratamientoId;
    }

    public void setTratamientoId(Long tratamientoId) {
        this.tratamientoId = tratamientoId;
    }

    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Integer getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(Integer duracionDias) {
        this.duracionDias = duracionDias;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }
}
