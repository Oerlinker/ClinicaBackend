package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.EmpleadoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.EmpleadoRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Empleado>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.getAllEmpleados());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createEmpleado(@RequestBody EmpleadoRegisterDTO dto) {
        try {
            Empleado nuevoEmpleado = empleadoService.createEmpleado(dto);
            return ResponseEntity.ok(nuevoEmpleado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmpleado(@PathVariable Long id, @RequestBody EmpleadoRegisterDTO dto) {
        try {
            Empleado updatedEmpleado = empleadoService.updateEmpleado(id, dto);
            return ResponseEntity.ok(updatedEmpleado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable Long id) {
        try {
            empleadoService.deleteEmpleado(id);
            return ResponseEntity.ok("Empleado eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/doctores")
    public ResponseEntity<?> getDoctores() {
        try {
            List<Empleado> doctores = empleadoService.getDoctores();
            return ResponseEntity.ok(doctores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/sin-departamento")
    public ResponseEntity<List<EmpleadoDTO>> getSinDepartamento() {
        return ResponseEntity.ok(empleadoService.listarSinDepartamento());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/departamento/{deptId}")
    public ResponseEntity<List<EmpleadoDTO>> getPorDepartamento(@PathVariable Long deptId) {
        return ResponseEntity.ok(empleadoService.listarPorDepartamento(deptId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{empId}/departamento/{deptId}")
    public ResponseEntity<?> asignarDepartamento(
            @PathVariable Long empId,
            @PathVariable Long deptId) {
        try {
            empleadoService.asignarDepartamento(empId, deptId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{empId}/sin-departamento")
    public ResponseEntity<?> quitarDepartamento(@PathVariable Long empId) {
        try {
            empleadoService.quitarDepartamento(empId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/todos")
    public ResponseEntity<List<EmpleadoDTO>> getTodos() {
        return ResponseEntity.ok(
                empleadoService.getAllEmpleados().stream()
                        .map(EmpleadoDTO::new)
                        .toList()
        );
    }
}
