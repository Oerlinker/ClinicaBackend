package com.clinicaoftalmologica.clinicaoftalmologicabackend.aop;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BitacoraService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class BitacoraAspect {

    private final BitacoraService bitacoraService;
    private final UsuarioService usuarioService;

    public BitacoraAspect(BitacoraService bitacoraService,
                          UsuarioService usuarioService) {
        this.bitacoraService = bitacoraService;
        this.usuarioService = usuarioService;
    }

    @Around("@annotation(loggable)")
    public Object logAround(ProceedingJoinPoint jp, Loggable loggable) throws Throwable {

        Object result = jp.proceed();


        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        Usuario usuario = usuarioService.obtenerPorUsername(username);


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


        bitacoraService.registrar(
                usuario,
                loggable.value(),
                entidad,
                entidadId,
                detalles
        );

        return result;
    }
}
