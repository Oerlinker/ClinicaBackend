package com.clinicaoftalmologica.clinicaoftalmologicabackend.aop;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BitacoraService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class BitacoraAspect {

    private final BitacoraService bitacoraService;
    private final UsuarioService usuarioService;
    private final HttpServletRequest request;

    @Autowired
    public BitacoraAspect(BitacoraService bitacoraService, UsuarioService usuarioService, HttpServletRequest request) {
        this.bitacoraService = bitacoraService;
        this.usuarioService = usuarioService;
        this.request = request;
    }

    @Pointcut("@annotation(com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable)")
    public void loggableMethods() {
    }

    @Around("loggableMethods() && @annotation(loggable)")
    public Object logAround(ProceedingJoinPoint jp, Loggable loggable) throws Throwable {
        Object result;
        try {
            // 1) intentar obtener usuario, pero sin hacer fallar el login
            String username = "SYSTEM";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                username = auth.getName();
            }
            Usuario usuario = null;
            try {
                usuario = usuarioService.obtenerPorUsername(username);
            } catch (Exception e) {
                // No hay usuario todavía (login), saltamos el registro
            }

            // 2) proceder al método objetivo (login, register, crear empleado, etc.)
            result = jp.proceed();

            // 3) si obtuvimos un usuario válido, grabamos la bitácora
            if (usuario != null) {
                // determinamos entidad, id, detalles (igual que antes)...
                String entidad = jp.getSignature().getDeclaringType().getSimpleName();
                Long entidadId = null;
                // extraer getId() de result o de args...
                // calcular detalles y IP
                String detalles = "Método: " + jp.getSignature().getName();
                String ip = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest().getRemoteAddr();

                bitacoraService.registrar(
                        usuario,
                        loggable.value(),
                        entidad,
                        entidadId,
                        detalles,
                        ip
                );
            }
        } catch (Throwable t) {
            // si algo en el AOP falla, lo capturamos y no interrumpimos la llamada original
            return jp.proceed();
        }
        return result;
    }
}