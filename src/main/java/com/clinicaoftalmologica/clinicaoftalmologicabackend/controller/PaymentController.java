package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CreatePaymentRequest;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Payment;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final String publishableKey;
    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

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
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String,String>> createCheckoutSession(
            @RequestBody CreatePaymentRequest req) throws StripeException {


        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendBaseUrl + "/pago-exitoso")
                .setCancelUrl(frontendBaseUrl + "/dashboard")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency(req.getCurrency())
                                        .setUnitAmount(req.getAmount())
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName("Pago cita #" + req.getCitaId())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
                )
                .build();

        Session session = Session.create(params);
        return ResponseEntity.ok(Map.of("url", session.getUrl()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PACIENTE')")
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<Payment> getPagoPorCita(@PathVariable Long citaId) {
        Payment pago = paymentService.getPaymentByCitaId(citaId);
        return ResponseEntity.ok(pago);
    }
}
