package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class TriajeCreateDTO {
    private Long citaId;
    private Double presionArterial;
    private Double frecuenciaCardiaca;
    private Double temperatura;
    private Double peso;
    private Double altura;
    private String comentarios;

    // getters y setters
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }

    public Double getPresionArterial() { return presionArterial; }
    public void setPresionArterial(Double presionArterial) {
        this.presionArterial = presionArterial;
    }

    public Double getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Double frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
