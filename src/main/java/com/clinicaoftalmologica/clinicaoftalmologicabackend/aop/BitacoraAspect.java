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
        String username = "SYSTEM";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            username = authentication.getName();
        } else {
            Object[] args = jp.getArgs();
            for (Object arg : args) {
                if (arg instanceof Usuario) {
                    username = ((Usuario) arg).getUsername();
                    break;
                }
            }
        }

        Usuario usuario = usuarioService.obtenerPorUsername(username);
        Object result = jp.proceed();
        Object[] args = jp.getArgs();
        String detalles = "Método: " + jp.getSignature().getName()
                + ", args=" + Arrays.toString(args);
        String entidad = jp.getSignature()
                .getDeclaringType().getSimpleName();
        Long entidadId = null;

        if (result != null) {
            try {
                Method getId = result.getClass().getMethod("getId");
                Object id = getId.invoke(result);
                if (id instanceof Long) {
                    entidadId = (Long) id;
                }
            } catch (Exception ignore) {
            }
        }

        if (entidadId == null && args != null) {
            for (Object arg : args) {
                if (arg instanceof Long) {
                    entidadId = (Long) arg;
                    break;
                }
                try {
                    Method getId = arg.getClass().getMethod("getId");
                    Object id = getId.invoke(arg);
                    if (id instanceof Long) {
                        entidadId = (Long) id;
                        break;
                    }
                } catch (Exception ignore) {
                }
            }
        }

        String ip = request.getRemoteAddr();

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