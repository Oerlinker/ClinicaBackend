import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BitacoraService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class BitacoraAspect {

    private final BitacoraService bitacoraService;
    private final UsuarioService usuarioService;

    public BitacoraAspect(BitacoraService bitacoraService, UsuarioService usuarioService) {
        this.bitacoraService = bitacoraService;
        this.usuarioService = usuarioService;
    }

    @Around("@annotation(loggable)")
    public Object logAround(ProceedingJoinPoint jp, Loggable loggable) throws Throwable {
        Object result = jp.proceed();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = null;

        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String username = authentication.getName();
            usuario = usuarioService.obtenerPorUsername(username);
        }

        Object[] args = jp.getArgs();
        String detalles = "Método: " + jp.getSignature().getName() + ", args=" + Arrays.toString(args);

        String entidad = jp.getSignature().getDeclaringType().getSimpleName();
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

        // Permite que usuario sea nulo para registro e inicio de sesión
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