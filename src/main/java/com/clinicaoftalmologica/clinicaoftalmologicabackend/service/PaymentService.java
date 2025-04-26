package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Payment;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.PaymentRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.UsuarioRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final CitaRepository citaRepo;
    private final UsuarioRepository usuarioRepo;

    public PaymentService(PaymentRepository paymentRepo,
                          CitaRepository citaRepo,
                          UsuarioRepository usuarioRepo) {
        this.paymentRepo = paymentRepo;
        this.citaRepo = citaRepo;
        this.usuarioRepo = usuarioRepo;
    }
    @Loggable("CREAR_PAGO")
    public String createPaymentIntent(long amount,
                                      String currency,
                                      Long citaId,
                                      Long pacienteId) throws StripeException {

        PaymentIntent intent = PaymentIntent.create(
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
                        .build()
        );


        Cita cita = citaRepo.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + citaId));
        Usuario paciente = usuarioRepo.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + pacienteId));


        Payment p = new Payment();
        p.setPaymentIntentId(intent.getId());
        p.setAmount(amount);
        p.setCurrency(currency);
        p.setStatus(intent.getStatus());
        p.setCita(cita);
        p.setPaciente(paciente);
        paymentRepo.save(p);

        return intent.getClientSecret();
    }
}