package com.clinicaoftalmologica.clinicaoftalmologicabackend.config;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Servicio;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.ServicioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ServicioSeeder implements CommandLineRunner {
    private final ServicioRepository repo;
    public ServicioSeeder(ServicioRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repo.count() == 0) {
            List<Servicio> inicial = List.of(
                    new Servicio("Consulta Control", "Revisión general de rutina", 5000L),
                    new Servicio("Consulta Especializada", "Evaluación especializada por patología", 7000L),
                    new Servicio("Preoperatorio", "Evaluación previa a cirugía", 8000L),
                    new Servicio("Postoperatorio", "Seguimiento post quirúrgico", 9000L)
            );
            repo.saveAll(inicial);
            System.out.println(">> Seed: servicios iniciales creados");
        }
    }
}