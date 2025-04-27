package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Payment;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.PaymentRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.CitaService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class WebhookController {

    private final PaymentRepository paymentRepo;
    private final CitaService citaService;
    @Value("${stripe.webhookSecret}")
    private String endpointSecret;

    public WebhookController(PaymentRepository paymentRepo,
                             CitaService citaService) {
        this.paymentRepo = paymentRepo;
        this.citaService = citaService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Webhook error: " + e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.ok("");
        }


        if ("payment_intent.succeeded".equals(event.getType())) {

            event.getDataObjectDeserializer()
                    .getObject()
                    .filter(obj -> obj instanceof PaymentIntent)
                    .map(obj -> (PaymentIntent) obj)
                    .ifPresent(intent -> {
                        paymentRepo.findByPaymentIntentId(intent.getId())
                                .ifPresent(p -> {
                                    p.setStatus(intent.getStatus());
                                    paymentRepo.save(p);
                                    try {
                                        citaService.markCitaPagada(p.getCita().getId());
                                    } catch (Exception ex) {
                                        System.err.println("Error marcando cita como pagada: " + ex.getMessage());
                                    }
                                });
                    });
        }


        return ResponseEntity.ok("");
    }
}
