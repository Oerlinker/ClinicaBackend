package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.EmpleadoRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cargo;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Rol;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CargoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.RolRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    public Empleado createEmpleado(EmpleadoRegisterDTO dto) throws Exception {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new Exception("Cargo no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        String base = (dto.getNombre() + dto.getApellido()).toLowerCase().replaceAll("\\s+", "");
        String username = base;
        int counter = 1;
        while (usuarioRepository.findByUsername(username).isPresent()) {
            username = base + counter;
            counter++;
        }
        usuario.setUsername(username);


        final String roleToAssign = cargo.getNombre().equalsIgnoreCase("Médico") ? "DOCTOR" : "EMPLEADO";
        Rol rol = rolRepository.findByNombre(roleToAssign)
                .orElseThrow(() -> new Exception("Rol " + roleToAssign + " no encontrado"));


        usuario.setRol(rol);

        Empleado empleado = new Empleado();
        empleado.setCargo(cargo);
        empleado.setUsuario(usuario);
        empleado.setEspecialidad(dto.getEspecialidad());
        if (dto.getFechaContratacion() != null && !dto.getFechaContratacion().isEmpty()) {
            empleado.setFechaContratacion(LocalDate.parse(dto.getFechaContratacion()));
        }
        if (dto.getSalario() != null && !dto.getSalario().isEmpty()) {
            empleado.setSalario(new BigDecimal(dto.getSalario()));
        }

        return empleadoRepository.save(empleado);
    }

    public Empleado updateEmpleado(Long id, Empleado updatedEmpleado) throws Exception {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new Exception("Empleado no encontrado"));
        empleado.setCargo(updatedEmpleado.getCargo());
        empleado.setEspecialidad(updatedEmpleado.getEspecialidad());
        empleado.setFechaContratacion(updatedEmpleado.getFechaContratacion());
        empleado.setSalario(updatedEmpleado.getSalario());
        return empleadoRepository.save(empleado);
    }

    public void deleteEmpleado(Long id) throws Exception {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new Exception("Empleado no encontrado"));
        empleadoRepository.delete(empleado);
    }

    public List<Empleado> getDoctores() throws Exception {
        Rol rolDoctor = rolRepository.findByNombre("DOCTOR")
                .orElseThrow(() -> new Exception("Rol DOCTOR no encontrado"));
        return empleadoRepository.findAll().stream()
                .filter(e -> e.getUsuario().getRol().equals(rolDoctor))
                .collect(Collectors.toList());
    }
}
