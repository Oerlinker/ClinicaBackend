package com.clinicaoftalmologica.clinicaoftalmologicabackend.config;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Patologia;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.PatologiaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PatologiaSeeder implements CommandLineRunner {

    private final PatologiaRepository patRepo;

    public PatologiaSeeder(PatologiaRepository patRepo) {
        this.patRepo = patRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (patRepo.count() == 0) {
            List<Patologia> inicial = List.of(
                    new Patologia("P001", "Glaucoma", "Presión intraocular elevada a largo plazo"),
                    new Patologia("P002", "Catarata", "Opacificación del cristalino"),
                    new Patologia("P003", "Miopía", "Dificultad para ver de lejos"),
                    new Patologia("P004", "Hipermetropía", "Dificultad para ver de cerca"),
                    new Patologia("P005", "Astigmatismo", "Visión borrosa por forma irregular de córnea")
            );
            patRepo.saveAll(inicial);
            System.out.println(">> Seed: patologías iniciales creadas");
        }
    }
}
