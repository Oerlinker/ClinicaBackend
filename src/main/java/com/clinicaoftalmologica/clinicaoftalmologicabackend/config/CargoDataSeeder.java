package com.clinicaoftalmologica.clinicaoftalmologicabackend.config;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cargo;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CargoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CargoDataSeeder {

    @Bean
    CommandLineRunner initCargos(CargoRepository cargoRepository) {
        return args -> {
            String[][] cargos = {
                    {"Médico", "Profesional de la salud encargado de diagnosticar y tratar a los pacientes."},
                    {"Enfermero", "Encargado de la atención y cuidados básicos de los pacientes."},
                    {"Secretaria", "Responsable de la administración y atención al paciente."},
                    {"Administrador", "Encargado de la gestión y administración de la clínica."}
            };

            for (String[] cargoData : cargos) {
                String nombre = cargoData[0];
                String descripcion = cargoData[1];
                if (cargoRepository.findByNombre(nombre).isEmpty()) {
                    Cargo cargo = new Cargo(nombre, descripcion);
                    cargoRepository.save(cargo);
                    System.out.println("Cargo creado: " + nombre);
                }
            }
        };
    }
}
