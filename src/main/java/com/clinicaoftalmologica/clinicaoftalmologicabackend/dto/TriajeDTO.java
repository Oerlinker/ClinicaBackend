package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Triaje;

import java.time.LocalDateTime;

public class TriajeDTO {
    private Long id;
    private Long citaId;
    private LocalDateTime fechaHoraRegistro;
    private Double presionArterial;
    private Double frecuenciaCardiaca;
    private Double temperatura;
    private Double peso;
    private Double altura;
    private String comentarios;

    public TriajeDTO(Triaje t) {
        this.id = t.getId();
        this.citaId = t.getCita().getId();
        this.fechaHoraRegistro = t.getFechaHoraRegistro();
        this.presionArterial = t.getPresionArterial();
        this.frecuenciaCardiaca = t.getFrecuenciaCardiaca();
        this.temperatura = t.getTemperatura();
        this.peso = t.getPeso();
        this.altura = t.getAltura();
        this.comentarios = t.getComentarios();
    }
    //getters
    public Long getId() { return id; }
    public Long getCitaId() { return citaId; }
    public LocalDateTime getFechaHoraRegistro() { return fechaHoraRegistro; }
    public Double getPresionArterial() { return presionArterial; }
    public Double getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public Double getTemperatura() { return temperatura; }
    public Double getPeso() { return peso; }
    public Double getAltura() { return altura; }
    public String getComentarios() { return comentarios; }
}