package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.MedicamentoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Medicamento;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Transactional(readOnly = true)
    public List<MedicamentoDTO> getAllMedicamentos() {
        return medicamentoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicamentoDTO> searchMedicamentosByNombre(String nombre) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MedicamentoDTO> getMedicamentoById(Long id) {
        return medicamentoRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public MedicamentoDTO createMedicamento(MedicamentoDTO medicamentoDTO) {
        Medicamento medicamento = convertToEntity(medicamentoDTO);
        medicamento.setActivo(true);
        Medicamento savedMedicamento = medicamentoRepository.save(medicamento);
        return convertToDTO(savedMedicamento);
    }

    @Transactional
    public Optional<MedicamentoDTO> updateMedicamento(Long id, MedicamentoDTO medicamentoDTO) {
        return medicamentoRepository.findById(id)
                .map(medicamentoExistente -> {
                    medicamentoExistente.setNombre(medicamentoDTO.getNombre());
                    medicamentoExistente.setDescripcion(medicamentoDTO.getDescripcion());
                    medicamentoExistente.setFabricante(medicamentoDTO.getFabricante());
                    medicamentoExistente.setEfectosSecundarios(medicamentoDTO.getEfectosSecundarios());
                    if (medicamentoDTO.getActivo() != null) {
                        medicamentoExistente.setActivo(medicamentoDTO.getActivo());
                    }
                    return convertToDTO(medicamentoRepository.save(medicamentoExistente));
                });
    }

    @Transactional
    public boolean deleteMedicamento(Long id) {
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    medicamento.setActivo(false);
                    medicamentoRepository.save(medicamento);
                    return true;
                })
                .orElse(false);
    }

    // Métodos de conversión entre entidad y DTO
    private MedicamentoDTO convertToDTO(Medicamento medicamento) {
        MedicamentoDTO medicamentoDTO = new MedicamentoDTO();
        medicamentoDTO.setId(medicamento.getId());
        medicamentoDTO.setNombre(medicamento.getNombre());
        medicamentoDTO.setDescripcion(medicamento.getDescripcion());
        medicamentoDTO.setFabricante(medicamento.getFabricante());
        medicamentoDTO.setEfectosSecundarios(medicamento.getEfectosSecundarios());
        medicamentoDTO.setActivo(medicamento.getActivo());
        return medicamentoDTO;
    }

    private Medicamento convertToEntity(MedicamentoDTO medicamentoDTO) {
        Medicamento medicamento = new Medicamento();
        if (medicamentoDTO.getId() != null) {
            medicamento.setId(medicamentoDTO.getId());
        }
        medicamento.setNombre(medicamentoDTO.getNombre());
        medicamento.setDescripcion(medicamentoDTO.getDescripcion());
        medicamento.setFabricante(medicamentoDTO.getFabricante());
        medicamento.setEfectosSecundarios(medicamentoDTO.getEfectosSecundarios());
        medicamento.setActivo(medicamentoDTO.getActivo() != null ? medicamentoDTO.getActivo() : true);
        return medicamento;
    }
}
