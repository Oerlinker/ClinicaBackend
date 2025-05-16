package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.PatologiaRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.PatologiaResponseDTO;

import java.util.List;

public interface PatologiaService {
    List<PatologiaResponseDTO> getAll();
    PatologiaResponseDTO getById(Long id);
    PatologiaResponseDTO create(PatologiaRegisterDTO dto);
    PatologiaResponseDTO update(Long id, PatologiaRegisterDTO dto);
    void delete(Long id);
}