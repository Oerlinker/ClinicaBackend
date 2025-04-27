package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Bitacora;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.BitacoraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class BitacoraService {

    private final BitacoraRepository bitacoraRepo;

    public BitacoraService(BitacoraRepository bitacoraRepo) {
        this.bitacoraRepo = bitacoraRepo;
    }


    public void registrar(Usuario usuario,
                          String accion,
                          String entidad,
                          Long entidadId,
                          String detalles) {
        Bitacora entry = new Bitacora(usuario, accion, entidad, entidadId, detalles);
        ZonedDateTime ahoraEnLaPaz = ZonedDateTime.now(ZoneId.of("America/La_Paz"));
        entry.setFecha(ahoraEnLaPaz.toLocalDateTime());
        bitacoraRepo.save(entry);
    }


    public List<Bitacora> getByUsuarioId(Long usuarioId) {
        return bitacoraRepo.findByUsuarioId(usuarioId);
    }

    public List<Bitacora> getAllEntriesOrdered() {
        return bitacoraRepo.findAllByOrderByFechaDesc();
    }

    public Page<Bitacora> getPage(Pageable pageable) {
        return bitacoraRepo.findAll(pageable);
    }

    public List<Bitacora> getByFechaRange(LocalDateTime desde,
                                          LocalDateTime hasta) {
        return bitacoraRepo.findByFechaBetween(desde, hasta)
                .stream()
                .sorted(Comparator.comparing(Bitacora::getFecha).reversed())
                .toList();
    }

    public List<Bitacora> getByUsuarioAndFecha(Long usuarioId,
                                               LocalDateTime desde,
                                               LocalDateTime hasta) {
        return bitacoraRepo.findByUsuarioIdAndFechaBetween(usuarioId, desde, hasta)
                .stream()
                .sorted(Comparator.comparing(Bitacora::getFecha).reversed())
                .toList();
    }
}
