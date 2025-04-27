package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Rol;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.RolRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Loggable("REGISTRAR_USUARIO")
    public Usuario registrarUsuario(Usuario usuario) throws Exception {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new Exception("El email ya está registrado");
        }


        String generatedUsername = generarUsername(usuario.getNombre(), usuario.getApellido());
        usuario.setUsername(generatedUsername);

        usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));


        if (usuario.getRol() == null) {
            Optional<Rol> rolOpt = rolRepository.findByNombre("PACIENTE");
            if (rolOpt.isPresent()) {
                usuario.setRol(rolOpt.get());
            } else {
                Rol rolPaciente = new Rol("PACIENTE");
                rolRepository.save(rolPaciente);
                usuario.setRol(rolPaciente);
            }
        }
        return usuarioRepository.save(usuario);
    }

    private String generarUsername(String nombre, String apellido) {

        String base = (nombre + apellido).toLowerCase().replaceAll("\\s+", "");
        String username = base;
        int counter = 1;

        while (usuarioRepository.findByUsername(username).isPresent()) {
            username = base + counter;
            counter++;
        }
        return username;
    }
    //probamos sin el Loggable para ver si ese es el error
    @Loggable("LOGIN_USUARIO")
    public Usuario loginUsuario(String email, String password) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (bCryptPasswordEncoder.matches(password, usuario.getPassword())) {
                return usuario;
            } else {
                throw new Exception("Contraseña incorrecta");
            }
        } else {
            throw new Exception("Usuario no encontrado");
        }
    }

    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + username)
                );
    }
}
