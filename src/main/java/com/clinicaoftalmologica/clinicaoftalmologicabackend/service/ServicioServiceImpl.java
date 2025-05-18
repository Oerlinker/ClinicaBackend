package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.ServicioRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.ServicioResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Servicio;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.ServicioRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {
    private final ServicioRepository repo;

    private ServicioResponseDTO toDto(Servicio s) {
        return new ServicioResponseDTO(
                s.getId(), s.getNombre(), s.getDescripcion(), s.getPrecio()
        );
    }

    @Override
    public List<ServicioResponseDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioResponseDTO obtenerPorId(Long id) {
        Servicio s = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Servicio no encontrado con id " + id
                ));
        return toDto(s);
    }

    @Override
    @Transactional
    public ServicioResponseDTO crear(ServicioRegisterDTO dto) {
        if (repo.existsByNombre(dto.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe un servicio con nombre " + dto.getNombre()
            );
        }
        Servicio s = new Servicio();
        s.setNombre(dto.getNombre());
        s.setDescripcion(dto.getDescripcion());
        s.setPrecio(dto.getPrecio());
        return toDto(repo.save(s));
    }

    @Override
    @Transactional
    public ServicioResponseDTO actualizar(Long id, ServicioRegisterDTO dto) {
        Servicio s = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Servicio no encontrado con id " + id
                ));
        s.setNombre(dto.getNombre());
        s.setDescripcion(dto.getDescripcion());
        s.setPrecio(dto.getPrecio());
        return toDto(repo.save(s));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Servicio no encontrado con id " + id
            );
        }
        repo.deleteById(id);
    }
}