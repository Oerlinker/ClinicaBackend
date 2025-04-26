package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Bitacora;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/bitacoras")
public class BitacoraController {

    @Autowired
    private BitacoraService bitacoraService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Bitacora>> getAllBitacoras() {
        List<Bitacora> logs = bitacoraService.getAllEntriesOrdered();
        return ResponseEntity.ok(logs);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Bitacora>> getByUsuario(@PathVariable Long userId) {
        List<Bitacora> logs = bitacoraService.getByUsuarioId(userId);
        return ResponseEntity.ok(logs);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/page")
    public ResponseEntity<Page<Bitacora>> getBitacorasPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        return ResponseEntity.ok(bitacoraService.getPage(pageable));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/fecha")
    public ResponseEntity<List<Bitacora>> getByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin    = hasta.atTime(LocalTime.MAX);
        List<Bitacora> logs = bitacoraService.getByFechaRange(inicio, fin);
        return ResponseEntity.ok(logs);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/usuario/{userId}/fecha")
    public ResponseEntity<List<Bitacora>> getByUsuarioYFecha(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin    = hasta.atTime(LocalTime.MAX);
        List<Bitacora> logs = bitacoraService.getByUsuarioAndFecha(userId, inicio, fin);
        return ResponseEntity.ok(logs);
    }
}
