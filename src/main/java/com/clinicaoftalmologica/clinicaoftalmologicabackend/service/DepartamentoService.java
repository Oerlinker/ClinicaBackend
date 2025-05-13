package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DepartamentoCreateDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DepartamentoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Departamento;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.DepartamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repo;

    @Transactional(readOnly = true)
    public List<DepartamentoDTO> listarTodos() {
        return repo.findAll()
                .stream()
                .map(DepartamentoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartamentoDTO obtenerPorId(Long id) {
        Departamento d = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Departamento no encontrado: " + id));
        return new DepartamentoDTO(d);
    }

    @Transactional
    public DepartamentoDTO crear(DepartamentoCreateDTO dto) {
        if (repo.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException(
                    "Ya existe un departamento con nombre: " + dto.getNombre());
        }
        Departamento d = new Departamento();
        d.setNombre(dto.getNombre());
        d.setDescripcion(dto.getDescripcion());
        return new DepartamentoDTO(repo.save(d));
    }

    @Transactional
    public DepartamentoDTO actualizar(Long id, DepartamentoCreateDTO dto) {
        Departamento d = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Departamento no encontrado: " + id));
        d.setNombre(dto.getNombre());
        d.setDescripcion(dto.getDescripcion());
        return new DepartamentoDTO(repo.save(d));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se puede eliminar. Departamento no existe: " + id);
        }
        repo.deleteById(id);
    }
}