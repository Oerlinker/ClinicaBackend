package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Bitacora;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.BitacoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    public List<Bitacora> getAllEntriesOrdered() {
        return bitacoraRepository.findAllByOrderByFechaDesc();
    }

    public List<Bitacora> getByUsuarioId(Long userId) {
        return bitacoraRepository.findByUsuarioId(userId);
    }

    public org.springframework.data.domain.Page<Bitacora> getPage(org.springframework.data.domain.Pageable pageable) {
        return bitacoraRepository.findAll(pageable);
    }

    public List<Bitacora> getByFechaRange(LocalDateTime desde, LocalDateTime hasta) {
        return bitacoraRepository.findByFechaBetween(desde, hasta);
    }

    public List<Bitacora> getByUsuarioAndFecha(Long userId, LocalDateTime desde, LocalDateTime hasta) {
        return bitacoraRepository.findByUsuarioIdAndFechaBetween(userId, desde, hasta);
    }

    public void registrar(Usuario usuario, String accion, String entidad, Long entidadId, String detalles) {
        Bitacora bitacora = new Bitacora();
        bitacora.setUsuario(usuario);
        bitacora.setAccion(accion);
        bitacora.setEntidad(entidad);
        bitacora.setEntidadId(entidadId);
        bitacora.setDetalles(detalles);
        bitacora.setFecha(LocalDateTime.now());
        bitacoraRepository.save(bitacora);
    }
}