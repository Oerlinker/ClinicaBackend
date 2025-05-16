package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.PatologiaRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.PatologiaResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Patologia;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.PatologiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatologiaServiceImpl implements PatologiaService {

    private final PatologiaRepository repo;

    @Autowired
    public PatologiaServiceImpl(PatologiaRepository repo) {
        this.repo = repo;
    }

    private PatologiaResponseDTO toDto(Patologia p) {
        return new PatologiaResponseDTO(p.getId(), p.getCodigo(), p.getNombre(), p.getDescripcion());
    }

    @Override
    public List<PatologiaResponseDTO> getAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatologiaResponseDTO getById(Long id) {
        Patologia p = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Patología no encontrada con id " + id));
        return toDto(p);
    }

    @Override
    public PatologiaResponseDTO create(PatologiaRegisterDTO dto) {
        if (repo.existsByCodigo(dto.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe una patología con código " + dto.getCodigo());
        }
        Patologia p = new Patologia();
        p.setCodigo(dto.getCodigo());
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        return toDto(repo.save(p));
    }

    @Override
    public PatologiaResponseDTO update(Long id, PatologiaRegisterDTO dto) {
        Patologia p = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Patología no encontrada con id " + id));
        p.setCodigo(dto.getCodigo());
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        return toDto(repo.save(p));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Patología no encontrada con id " + id);
        }
        repo.deleteById(id);
    }
}