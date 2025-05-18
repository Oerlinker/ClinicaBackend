package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.HistorialClinicoDTO;

public interface HistorialClinicoService {
    HistorialClinicoDTO getHistorialPorUsuario(Long usuarioId);
}