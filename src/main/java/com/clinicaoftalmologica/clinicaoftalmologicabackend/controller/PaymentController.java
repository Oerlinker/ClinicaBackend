package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CreatePaymentRequest;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Payment;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final String publishableKey;

    public PaymentController(PaymentService paymentService,
                             @Value("${stripe.publishableKey}") String publishableKey) {
        this.paymentService = paymentService;
        this.publishableKey = publishableKey;
    }


    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        return ResponseEntity.ok(Map.of("publicKey", publishableKey));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PACIENTE')")
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String,String>> createPaymentIntent(
            @RequestBody CreatePaymentRequest req) throws StripeException {

        String clientSecret = paymentService.createPaymentIntent(
                req.getAmount(),
                req.getCurrency(),
                req.getCitaId(),
                req.getPacienteId()
        );
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PACIENTE')")
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<Payment> getPagoPorCita(@PathVariable Long citaId) {
        Payment pago = paymentService.getPaymentByCitaId(citaId);
        return ResponseEntity.ok(pago);
    }
}
