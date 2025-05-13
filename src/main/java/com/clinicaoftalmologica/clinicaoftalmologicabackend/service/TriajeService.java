package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.TriajeCreateDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.TriajeDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Triaje;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.TriajeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TriajeService {

    @Autowired
    private TriajeRepository repo;

    @Autowired
    private CitaRepository citaRepo;

    @Transactional
    public TriajeDTO crear(TriajeCreateDTO dto) {
        Cita cita = citaRepo.findById(dto.getCitaId())
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada"));

        if (repo.findByCitaId(cita.getId()).isPresent()) {
            throw new IllegalStateException("Ya existe un triaje para esta cita");
        }

        Triaje t = new Triaje();
        t.setCita(cita);
        t.setFechaHoraRegistro(LocalDateTime.now());
        t.setPresionArterial(dto.getPresionArterial());
        t.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        t.setTemperatura(dto.getTemperatura());
        t.setPeso(dto.getPeso());
        t.setAltura(dto.getAltura());
        t.setComentarios(dto.getComentarios());

        return new TriajeDTO(repo.save(t));
    }

    @Transactional(readOnly = true)
    public TriajeDTO obtenerPorCita(Long citaId) {
        return repo.findByCitaId(citaId)
                .map(TriajeDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Triaje no encontrado"));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Triaje no encontrado");
        }
        repo.deleteById(id);
    }
}