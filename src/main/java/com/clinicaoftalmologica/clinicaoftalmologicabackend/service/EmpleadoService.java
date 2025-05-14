package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.EmpleadoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.EmpleadoRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.*;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CargoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.RolRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EspecialidadRepository;
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

    @Autowired
    private EspecialidadRepository especialidadRepo;

    @Autowired
    private DisponibilidadInitService disponibilidadInitService;

    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    @Loggable("REGISTRAR_EMPLEADO")
    public Empleado createEmpleado(EmpleadoRegisterDTO dto) throws Exception {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new Exception("Cargo no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));


        String base = (dto.getNombre() + dto.getApellido())
                .toLowerCase()
                .replaceAll("\\s+", "");
        String username = base;
        int counter = 1;
        while (usuarioRepository.findByUsername(username).isPresent()) {
            username = base + counter++;
        }
        usuario.setUsername(username);


        final String roleToAssign;
        if (cargo.getNombre().equalsIgnoreCase("Médico")) {
            roleToAssign = "DOCTOR";
        } else if (cargo.getNombre().equalsIgnoreCase("Administrador")) {
            roleToAssign = "ADMIN";
        } else if (cargo.getNombre().equalsIgnoreCase("Secretaria")) {
            roleToAssign = "SECRETARIA";
        } else if (cargo.getNombre().equalsIgnoreCase("Enfermero")) {
            roleToAssign = "ENFERMERA";
        } else {
            roleToAssign = "EMPLEADO";
        }

        Rol rol = rolRepository.findByNombre(roleToAssign)
                .orElseThrow(() -> new Exception("Rol " + roleToAssign + " no encontrado"));
        usuario.setRol(rol);

        Empleado empleado = new Empleado();
        empleado.setCargo(cargo);
        empleado.setUsuario(usuario);


        if (dto.getEspecialidadId() != null) {
            Especialidad esp = especialidadRepo.findById(dto.getEspecialidadId())
                    .orElseThrow(() -> new Exception("Especialidad no encontrada"));
            empleado.setEspecialidad(esp);
        } else {
            empleado.setEspecialidad(null);
        }

        if (dto.getFechaContratacion() != null && !dto.getFechaContratacion().isEmpty()) {
            empleado.setFechaContratacion(LocalDate.parse(dto.getFechaContratacion()));
        }
        if (dto.getSalario() != null && !dto.getSalario().isEmpty()) {
            empleado.setSalario(new BigDecimal(dto.getSalario()));
        }

        empleado = empleadoRepository.save(empleado);

        if (roleToAssign.equals("DOCTOR")) {
            disponibilidadInitService.crearDisponibilidadesParaDoctor(
                    empleado.getId(),
                    LocalDate.now(),
                    LocalDate.now().plusDays(30)
            );
        }

        return empleado;
    }

    @Loggable("ACTUALIZAR_EMPLEADO")
    public Empleado updateEmpleado(Long id, Empleado updatedEmpleado) throws Exception {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new Exception("Empleado no encontrado"));

        empleado.setCargo(updatedEmpleado.getCargo());


        empleado.setEspecialidad(updatedEmpleado.getEspecialidad());

        empleado.setFechaContratacion(updatedEmpleado.getFechaContratacion());
        empleado.setSalario(updatedEmpleado.getSalario());
        return empleadoRepository.save(empleado);
    }

    @Loggable("ELIMINAR_EMPLEADO")
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

    public List<EmpleadoDTO> listarSinDepartamento() {
        return empleadoRepository.findByDepartamentoIsNull()
                .stream().map(EmpleadoDTO::new).toList();
    }

    public List<EmpleadoDTO> listarPorDepartamento(Long deptId) {
        return empleadoRepository.findByDepartamentoId(deptId)
                .stream().map(EmpleadoDTO::new).toList();
    }

    public void asignarDepartamento(Long empId, Long deptId) throws Exception {
        Empleado empleado = empleadoRepository.findById(empId)
                .orElseThrow(() -> new Exception("Empleado no encontrado"));
        Departamento depto = new Departamento();
        depto.setId(deptId);
        empleado.setDepartamento(depto);
        empleadoRepository.save(empleado);
    }

    public void quitarDepartamento(Long empId) throws Exception {
        Empleado empleado = empleadoRepository.findById(empId)
                .orElseThrow(() -> new Exception("Empleado no encontrado"));
        empleado.setDepartamento(null);
        empleadoRepository.save(empleado);
    }

    public List<EmpleadoDTO> listarTodos() {
        return empleadoRepository.findAll()
                .stream().map(EmpleadoDTO::new).toList();
    }
}
