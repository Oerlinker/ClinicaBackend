package com.clinicaoftalmologica.clinicaoftalmologicabackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "triaje")
public class Triaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Cita cita;

    @Column(nullable = false)
    private LocalDateTime fechaHoraRegistro;

    private Double presionArterial;
    private Double frecuenciaCardiaca;
    private Double temperatura;
    private Double peso;
    private Double altura;

    @Column(length = 1000)
    private String comentarios;


    public Triaje() {}
    //constructor
    public Triaje(Cita cita,
                  LocalDateTime fechaHoraRegistro,
                  Double presionArterial,
                  Double frecuenciaCardiaca,
                  Double temperatura,
                  Double peso,
                  Double altura,
                  String comentarios) {
        this.cita = cita;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.presionArterial = presionArterial;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.temperatura = temperatura;
        this.peso = peso;
        this.altura = altura;
        this.comentarios = comentarios;
    }
    //getter y setter

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }

    public LocalDateTime getFechaHoraRegistro() { return fechaHoraRegistro; }
    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

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
