package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CreatePaymentRequest;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Payment;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.PaymentService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;
    private final String publishableKey;

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    private final UsuarioService usuarioService;

    @Autowired
    public PaymentController(PaymentService paymentService,
                             UsuarioService usuarioService,
                             @Value("${stripe.publishableKey}") String publishableKey) {
        this.paymentService = paymentService;
        this.usuarioService = usuarioService;
        this.publishableKey = publishableKey;
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        return ResponseEntity.ok(Map.of("publicKey", publishableKey));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PACIENTE','SECRETARIA')")
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestBody CreatePaymentRequest req,
            Principal principal
    ) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendBaseUrl + "/pago-exitoso")
                    .setCancelUrl(frontendBaseUrl + "/dashboard")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(req.getCurrency())
                                    .setUnitAmount(req.getAmount())
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Pago cita #" + req.getCitaId())
                                            .build())
                                    .build())
                            .build())
                    .build();

            Session session = Session.create(params);

            Usuario paciente = usuarioService.obtenerPorUsername(principal.getName());
            paymentService.savePaymentRecord(
                    session.getPaymentIntent(),
                    req.getAmount(),
                    req.getCurrency(),
                    req.getCitaId(),
                    paciente.getId()
            );

            return ResponseEntity.ok(Map.of("url", session.getUrl()));

        } catch (StripeException e) {
            logger.error("Error al crear la sesión de Stripe Checkout: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la sesión de Stripe Checkout: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear la sesión de pago: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al crear la sesión de pago: " + e.getMessage());
        }
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','PACIENTE')")
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<Payment> getPagoPorCita(@PathVariable Long citaId) {
        Payment pago = paymentService.getPaymentByCitaId(citaId);
        return ResponseEntity.ok(pago);
    }
}
