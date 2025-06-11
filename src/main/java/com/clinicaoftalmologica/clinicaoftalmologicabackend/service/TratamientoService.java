package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.MedicamentoTratamientoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.TratamientoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Medicamento;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.MedicamentoTratamiento;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Tratamiento;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.AtencionRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.MedicamentoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.MedicamentoTratamientoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.TratamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TratamientoService {

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private AtencionRepository atencionRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private MedicamentoTratamientoRepository medicamentoTratamientoRepository;

    @Transactional(readOnly = true)
    public List<TratamientoDTO> getAllTratamientos() {
        return tratamientoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TratamientoDTO> getTratamientosByAtencion(Long atencionId) {
        return tratamientoRepository.findByAtencionId(atencionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TratamientoDTO> getTratamientoById(Long id) {
        return tratamientoRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public TratamientoDTO createTratamiento(TratamientoDTO tratamientoDTO) throws Exception {
        // Verificar que la atención existe
        Atencion atencion = atencionRepository.findById(tratamientoDTO.getAtencionId())
                .orElseThrow(() -> new Exception("Atención no encontrada con ID: " + tratamientoDTO.getAtencionId()));

        Tratamiento tratamiento = convertToEntity(tratamientoDTO);
        tratamiento.setAtencion(atencion);
        tratamiento.setActivo(true);

        Tratamiento savedTratamiento = tratamientoRepository.save(tratamiento);

        // Si hay medicamentos en el DTO, guardarlos
        if (tratamientoDTO.getMedicamentos() != null && !tratamientoDTO.getMedicamentos().isEmpty()) {
            for (MedicamentoTratamientoDTO medDTO : tratamientoDTO.getMedicamentos()) {
                asociarMedicamentoATratamiento(savedTratamiento.getId(), medDTO);
            }

            // Refrescar el tratamiento para obtener los medicamentos asociados
            savedTratamiento = tratamientoRepository.findById(savedTratamiento.getId()).orElse(savedTratamiento);
        }

        return convertToDTO(savedTratamiento);
    }

    @Transactional
    public Optional<TratamientoDTO> updateTratamiento(Long id, TratamientoDTO tratamientoDTO) throws Exception {
        return tratamientoRepository.findById(id)
                .map(tratamientoExistente -> {
                    tratamientoExistente.setNombre(tratamientoDTO.getNombre());
                    tratamientoExistente.setDescripcion(tratamientoDTO.getDescripcion());
                    tratamientoExistente.setDuracionDias(tratamientoDTO.getDuracionDias());
                    tratamientoExistente.setFechaInicio(tratamientoDTO.getFechaInicio());
                    tratamientoExistente.setFechaFin(tratamientoDTO.getFechaFin());
                    if (tratamientoDTO.getActivo() != null) {
                        tratamientoExistente.setActivo(tratamientoDTO.getActivo());
                    }

                    Tratamiento savedTratamiento = tratamientoRepository.save(tratamientoExistente);
                    return convertToDTO(savedTratamiento);
                });
    }

    @Transactional
    public boolean deleteTratamiento(Long id) {
        return tratamientoRepository.findById(id)
                .map(tratamiento -> {
                    tratamiento.setActivo(false);
                    tratamientoRepository.save(tratamiento);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public MedicamentoTratamientoDTO asociarMedicamentoATratamiento(Long tratamientoId, MedicamentoTratamientoDTO medicamentoTratamientoDTO) throws Exception {
        // Verificar que el tratamiento existe
        Tratamiento tratamiento = tratamientoRepository.findById(tratamientoId)
                .orElseThrow(() -> new Exception("Tratamiento no encontrado con ID: " + tratamientoId));

        // Verificar que el medicamento existe
        Medicamento medicamento = medicamentoRepository.findById(medicamentoTratamientoDTO.getMedicamentoId())
                .orElseThrow(() -> new Exception("Medicamento no encontrado con ID: " + medicamentoTratamientoDTO.getMedicamentoId()));

        // Crear la asociación
        MedicamentoTratamiento medicamentoTratamiento = new MedicamentoTratamiento();
        medicamentoTratamiento.setTratamiento(tratamiento);
        medicamentoTratamiento.setMedicamento(medicamento);
        medicamentoTratamiento.setDosis(medicamentoTratamientoDTO.getDosis());
        medicamentoTratamiento.setUnidadMedida(medicamentoTratamientoDTO.getUnidadMedida());
        medicamentoTratamiento.setFrecuencia(medicamentoTratamientoDTO.getFrecuencia());
        medicamentoTratamiento.setDuracionDias(medicamentoTratamientoDTO.getDuracionDias());
        medicamentoTratamiento.setViaAdministracion(medicamentoTratamientoDTO.getViaAdministracion());
        medicamentoTratamiento.setInstrucciones(medicamentoTratamientoDTO.getInstrucciones());

        MedicamentoTratamiento savedMedicamentoTratamiento = medicamentoTratamientoRepository.save(medicamentoTratamiento);

        return convertToDTO(savedMedicamentoTratamiento);
    }

    @Transactional
    public boolean eliminarMedicamentoDeTratamiento(Long tratamientoId, Long medicamentoTratamientoId) throws Exception {
        // Verificar que ambas entidades existen
        Tratamiento tratamiento = tratamientoRepository.findById(tratamientoId)
                .orElseThrow(() -> new Exception("Tratamiento no encontrado con ID: " + tratamientoId));

        MedicamentoTratamiento medicamentoTratamiento = medicamentoTratamientoRepository.findById(medicamentoTratamientoId)
                .orElseThrow(() -> new Exception("Asociación no encontrada con ID: " + medicamentoTratamientoId));

        // Verificar que el medicamento pertenece al tratamiento
        if (!medicamentoTratamiento.getTratamiento().getId().equals(tratamientoId)) {
            throw new Exception("El medicamento no pertenece al tratamiento especificado");
        }

        medicamentoTratamientoRepository.delete(medicamentoTratamiento);
        return true;
    }

    @Transactional(readOnly = true)
    public List<MedicamentoTratamientoDTO> getMedicamentosByTratamiento(Long tratamientoId) throws Exception {
        // Verificar que el tratamiento existe
        if (!tratamientoRepository.existsById(tratamientoId)) {
            throw new Exception("Tratamiento no encontrado con ID: " + tratamientoId);
        }

        return medicamentoTratamientoRepository.findByTratamientoId(tratamientoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversión entre entidad y DTO
    private TratamientoDTO convertToDTO(Tratamiento tratamiento) {
        TratamientoDTO tratamientoDTO = new TratamientoDTO();
        tratamientoDTO.setId(tratamiento.getId());
        tratamientoDTO.setNombre(tratamiento.getNombre());
        tratamientoDTO.setDescripcion(tratamiento.getDescripcion());
        tratamientoDTO.setDuracionDias(tratamiento.getDuracionDias());
        tratamientoDTO.setFechaInicio(tratamiento.getFechaInicio());
        tratamientoDTO.setFechaFin(tratamiento.getFechaFin());
        tratamientoDTO.setAtencionId(tratamiento.getAtencion() != null ? tratamiento.getAtencion().getId() : null);
        tratamientoDTO.setActivo(tratamiento.getActivo());

        // Convertir los medicamentos asociados a DTO
        Set<MedicamentoTratamientoDTO> medicamentosDTOs = tratamiento.getMedicamentos().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
        tratamientoDTO.setMedicamentos(medicamentosDTOs);

        return tratamientoDTO;
    }

    private Tratamiento convertToEntity(TratamientoDTO tratamientoDTO) {
        Tratamiento tratamiento = new Tratamiento();
        if (tratamientoDTO.getId() != null) {
            tratamiento.setId(tratamientoDTO.getId());
        }
        tratamiento.setNombre(tratamientoDTO.getNombre());
        tratamiento.setDescripcion(tratamientoDTO.getDescripcion());
        tratamiento.setDuracionDias(tratamientoDTO.getDuracionDias());
        tratamiento.setFechaInicio(tratamientoDTO.getFechaInicio());
        tratamiento.setFechaFin(tratamientoDTO.getFechaFin());
        tratamiento.setActivo(tratamientoDTO.getActivo() != null ? tratamientoDTO.getActivo() : true);
        return tratamiento;
    }

    private MedicamentoTratamientoDTO convertToDTO(MedicamentoTratamiento medicamentoTratamiento) {
        MedicamentoTratamientoDTO dto = new MedicamentoTratamientoDTO();
        dto.setId(medicamentoTratamiento.getId());
        dto.setTratamientoId(medicamentoTratamiento.getTratamiento() != null ?
                medicamentoTratamiento.getTratamiento().getId() : null);
        dto.setMedicamentoId(medicamentoTratamiento.getMedicamento() != null ?
                medicamentoTratamiento.getMedicamento().getId() : null);
        dto.setNombreMedicamento(medicamentoTratamiento.getMedicamento() != null ?
                medicamentoTratamiento.getMedicamento().getNombre() : null);
        dto.setDosis(medicamentoTratamiento.getDosis());
        dto.setUnidadMedida(medicamentoTratamiento.getUnidadMedida());
        dto.setFrecuencia(medicamentoTratamiento.getFrecuencia());
        dto.setDuracionDias(medicamentoTratamiento.getDuracionDias());
        dto.setViaAdministracion(medicamentoTratamiento.getViaAdministracion());
        dto.setInstrucciones(medicamentoTratamiento.getInstrucciones());
        return dto;
    }
}
