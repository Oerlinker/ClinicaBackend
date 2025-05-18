package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DoctorDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.HistorialClinicoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistorialClinicoServiceImpl implements HistorialClinicoService {
    private final AntecedenteService antecedenteService;
    private final AtencionService atencionService;
    private final CitaService citaService;

    @Override
    public HistorialClinicoDTO getHistorialPorUsuario(Long usuarioId) {
        HistorialClinicoDTO dto = new HistorialClinicoDTO();
        // Antecedentes
        dto.setAntecedentes(antecedenteService.listarPorUsuario(usuarioId));
        // Atenciones
        dto.setAtenciones(atencionService.listarPorUsuario(usuarioId));
        // Citas
        List<Cita> citas = citaService.listarPorUsuario(usuarioId);
        dto.setCitas(citas);
        // Médicos visitados
        Set<DoctorDTO> docs = citas.stream()
                .map(cita -> {
                    var emp = cita.getDoctor();
                    var usr = emp.getUsuario();
                    return new DoctorDTO(
                            usr.getId(), emp.getId(), usr.getNombre(), usr.getApellido()
                    );
                })
                .collect(Collectors.toSet());
        dto.setMedicosVisitados(docs);
        return dto;
    }
}