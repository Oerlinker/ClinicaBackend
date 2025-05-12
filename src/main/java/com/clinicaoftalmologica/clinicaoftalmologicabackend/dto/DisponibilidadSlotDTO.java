package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class DisponibilidadSlotDTO {
    private String hora;
    private int cuposRestantes;

    public DisponibilidadSlotDTO() { }

    public DisponibilidadSlotDTO(String hora, int cuposRestantes) {
        this.hora = hora;
        this.cuposRestantes = cuposRestantes;
    }

    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public int getCuposRestantes() {
        return cuposRestantes;
    }
    public void setCuposRestantes(int cuposRestantes) {
        this.cuposRestantes = cuposRestantes;
    }
}
