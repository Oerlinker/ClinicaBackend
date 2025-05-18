package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AntecedenteRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AntecedenteResponseDTO;

import java.util.List;

public interface AntecedenteService {
    List<AntecedenteResponseDTO> listarPorUsuario(Long usuarioId);
    AntecedenteResponseDTO crear(AntecedenteRegisterDTO dto);
    AntecedenteResponseDTO actualizar(Long id, AntecedenteRegisterDTO dto);
    void eliminar(Long id);
}
