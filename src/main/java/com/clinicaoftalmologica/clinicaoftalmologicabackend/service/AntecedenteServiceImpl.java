package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AntecedenteRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AntecedenteResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Antecedente;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.AntecedenteRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AntecedenteServiceImpl implements AntecedenteService {

    private final AntecedenteRepository repo;
    private final UsuarioRepository usuarioRepo;

    private AntecedenteResponseDTO toDto(Antecedente a) {
        return new AntecedenteResponseDTO(
                a.getId(), a.getTipo(), a.getDescripcion(), a.getFechaRegistro()
        );
    }

    @Override
    public List<AntecedenteResponseDTO> listarPorUsuario(Long usuarioId) {
        return repo.findByUsuarioId(usuarioId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AntecedenteResponseDTO crear(AntecedenteRegisterDTO dto) {
        Usuario u = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
        Antecedente a = new Antecedente();
        a.setUsuario(u);
        a.setTipo(dto.getTipo());
        a.setDescripcion(dto.getDescripcion());
        a.setFechaRegistro(LocalDateTime.now());
        return toDto(repo.save(a));
    }

    @Override
    @Transactional
    public AntecedenteResponseDTO actualizar(Long id, AntecedenteRegisterDTO dto) {
        Antecedente a = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Antecedente no encontrado"
                ));
        a.setTipo(dto.getTipo());
        a.setDescripcion(dto.getDescripcion());
        return toDto(repo.save(a));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Antecedente no encontrado"
            );
        }
        repo.deleteById(id);
    }
}