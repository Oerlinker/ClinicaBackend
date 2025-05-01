package com.clinicaoftalmologica.clinicaoftalmologicabackend.aop;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BitacoraService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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
    private final UsuarioRepository usuarioRepository;

    public BitacoraAspect(BitacoraService bitacoraService,
                          UsuarioRepository usuarioRepository) {
        this.bitacoraService    = bitacoraService;
        this.usuarioRepository  = usuarioRepository;
    }


    @Around("@annotation(loggable) && " +
            "!execution(* com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService.obtenerPorUsername(..))")
    public Object logAround(ProceedingJoinPoint jp, Loggable loggable) throws Throwable {

        Object result = jp.proceed();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            usuario = usuarioRepository
                    .findByUsername(username)
                    .orElse(null);
        }


        Object[] args = jp.getArgs();
        String detalles = "Método: " + jp.getSignature().getName()
                + ", args=" + Arrays.toString(args);

        String entidad = jp.getSignature()
                .getDeclaringType().getSimpleName();

        Long entidadId = null;
        if (result != null) {
            try {
                Method m = result.getClass().getMethod("getId");
                Object id = m.invoke(result);
                if (id instanceof Long) entidadId = (Long) id;
            } catch (Exception ignored) {}
        }
        if (entidadId == null && args != null) {
            for (Object a : args) {
                if (a instanceof Long) {
                    entidadId = (Long) a;
                    break;
                }
                try {
                    Method m = a.getClass().getMethod("getId");
                    Object id = m.invoke(a);
                    if (id instanceof Long) {
                        entidadId = (Long) id;
                        break;
                    }
                } catch (Exception ignore) {}
            }
        }


        String ip = null;
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            ip = sra.getRequest().getRemoteAddr();
        }


        bitacoraService.registrar(
                usuario,
                loggable.value(),
                entidad,
                entidadId,
                detalles,
                ip
        );

        return result;
    }
}
