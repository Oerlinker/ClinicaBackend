package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.ServicioRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.ServicioResponseDTO;
import java.util.List;

public interface ServicioService {
    List<ServicioResponseDTO> listarTodos();
    ServicioResponseDTO obtenerPorId(Long id);
    ServicioResponseDTO crear(ServicioRegisterDTO dto);
    ServicioResponseDTO actualizar(Long id, ServicioRegisterDTO dto);
    void eliminar(Long id);
}