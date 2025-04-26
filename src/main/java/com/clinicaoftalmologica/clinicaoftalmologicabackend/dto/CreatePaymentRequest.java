package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

public class CreatePaymentRequest {
    private Long amount;
    private String currency;
    private Long citaId;
    private Long pacienteId;

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
}
