package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.security.JwtUtil;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BitacoraService bitacoraService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(
            @RequestBody Usuario usuario,
            HttpServletRequest request
    ) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);

            String ip = request.getRemoteAddr();
            String detalles = "Registro de usuario exitoso. Email: "
                    + nuevoUsuario.getEmail()
                    + ". IP: " + ip;
            bitacoraService.registrar(
                    nuevoUsuario,
                    "REGISTRAR_USUARIO",
                    "Usuario",
                    nuevoUsuario.getId(),
                    detalles,
                    ip
            );
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {

            String ip = request.getRemoteAddr();
            String detalles = "Registro de usuario fallido. Email: "
                    + usuario.getEmail()
                    + ". Error: " + e.getMessage()
                    + ". IP: " + ip;
            bitacoraService.registrar(
                    null,
                    "REGISTRAR_USUARIO_FALLIDO",
                    "Authentication",
                    null,
                    detalles,
                    ip
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        try {
            Usuario usuarioLogueado = usuarioService.loginUsuario(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            String token = jwtUtil.generateToken(usuarioLogueado);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", usuarioLogueado);


            String ip = request.getRemoteAddr();
            String detalles = "Inicio de sesión exitoso. Email: "
                    + usuarioLogueado.getEmail()
                    + ". IP: " + ip;
            bitacoraService.registrar(
                    usuarioLogueado,
                    "LOGIN_EXITOSO",
                    "Usuario",
                    usuarioLogueado.getId(),
                    detalles,
                    ip
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            String ip = request.getRemoteAddr();
            String detalles = "Intento de login fallido. Email: "
                    + loginRequest.getEmail()
                    + ". IP: " + ip;
            bitacoraService.registrar(
                    null,
                    "LOGIN_FALLIDO",
                    "Authentication",
                    null,
                    detalles,
                    ip
            );

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
