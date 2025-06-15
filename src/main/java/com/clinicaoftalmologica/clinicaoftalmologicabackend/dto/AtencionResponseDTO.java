package com.clinicaoftalmologica.clinicaoftalmologicabackend.dto;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Tratamiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AtencionResponseDTO {
    private Long id;
    private LocalDate fecha;
    private String motivo;
    private String diagnostico;
    private String observaciones;
    private PatologiaResponseDTO patologia;
    private List<TratamientoDTO> tratamientos;

    // Nuevos campos para mostrar información de paciente y doctor
    private Long pacienteId;
    private String pacienteNombre;
    private Long doctorId;
    private String doctorNombre;

    public AtencionResponseDTO(Atencion a) {
        this.id = a.getId();
        this.fecha = a.getFecha();
        this.motivo = a.getMotivo();
        this.diagnostico = a.getDiagnostico();
        this.observaciones = a.getObservaciones();

        // Obtener datos del paciente
        if (a.getCita() != null && a.getCita().getPaciente() != null) {
            var paciente = a.getCita().getPaciente();
            this.pacienteId = paciente.getId();
            this.pacienteNombre = paciente.getNombre() + " " + paciente.getApellido();
        }

        // Obtener datos del doctor
        if (a.getCita() != null && a.getCita().getDoctor() != null) {
            var doctor = a.getCita().getDoctor();
            this.doctorId = doctor.getId();
            if (doctor.getUsuario() != null) {
                this.doctorNombre = doctor.getUsuario().getNombre() + " " + doctor.getUsuario().getApellido();
            }
        }

        if (a.getPatologia() != null) {
            var p = a.getPatologia();
            this.patologia = new PatologiaResponseDTO(
                    p.getId(),
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion()
            );
        }

        // Convertir los tratamientos a DTOs
        if (a.getTratamientos() != null && !a.getTratamientos().isEmpty()) {
            this.tratamientos = a.getTratamientos().stream()
                .filter(t -> t.getActivo() != null && t.getActivo())
                .map(this::convertTratamientoToDTO)
                .collect(Collectors.toList());
        } else {
            // Inicializar como lista vacía para evitar problemas de null
            this.tratamientos = new ArrayList<>();
        }
    }


    private TratamientoDTO convertTratamientoToDTO(Tratamiento tratamiento) {
        TratamientoDTO dto = new TratamientoDTO();
        dto.setId(tratamiento.getId());
        dto.setNombre(tratamiento.getNombre());
        dto.setDescripcion(tratamiento.getDescripcion());
        dto.setDuracionDias(tratamiento.getDuracionDias());
        dto.setFechaInicio(tratamiento.getFechaInicio());
        dto.setFechaFin(tratamiento.getFechaFin());
        dto.setAtencionId(tratamiento.getAtencion() != null ? tratamiento.getAtencion().getId() : null);
        dto.setActivo(tratamiento.getActivo());

        return dto;
    }

    // getters y setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public PatologiaResponseDTO getPatologia() {
        return patologia;
    }

    public void setPatologia(PatologiaResponseDTO patologia) {
        this.patologia = patologia;
    }

    public List<TratamientoDTO> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<TratamientoDTO> tratamientos) {
        this.tratamientos = tratamientos;
    }

    // Getters y setters para los nuevos campos
    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorNombre() {
        return doctorNombre;
    }

    public void setDoctorNombre(String doctorNombre) {
        this.doctorNombre = doctorNombre;
    }
}
