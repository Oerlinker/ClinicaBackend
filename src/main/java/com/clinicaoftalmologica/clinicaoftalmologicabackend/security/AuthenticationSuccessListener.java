// AuthenticationSuccessListener.java
package com.clinicaoftalmologica.clinicaoftalmologicabackend.security;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BitacoraService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final BitacoraService bitacoraService;
    private final UsuarioService usuarioService;

    public AuthenticationSuccessListener(BitacoraService bitacoraService,
                                         UsuarioService usuarioService) {
        this.bitacoraService = bitacoraService;
        this.usuarioService = usuarioService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        Usuario usuario = usuarioService.obtenerPorUsername(username);

        String ip = null;
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            HttpServletRequest req = sra.getRequest();
            ip = req.getRemoteAddr();
        }

        String detalles = "Inicio de sesión exitoso. IP: " + (ip != null ? ip : "desconocida");
        bitacoraService.registrar(
                usuario,
                "LOGIN_EXITOSO",
                "Usuario",
                usuario.getId(),
                detalles,
                ip
        );
    }
}
