
package com.clinicaoftalmologica.clinicaoftalmologicabackend.config;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Especialidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EspecialidadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EspecialidadDataSeeder {
    @Bean
    CommandLineRunner initEspecialidades(EspecialidadRepository especialidadRepository) {
        return args -> {
            String[][] especialidadesData = {
                    {"Oftalmología", "Especialidad médica que se centra en la salud de los ojos."},
                    {"Optometría", "Especialidad que se encarga de la medición de la vista y la adaptación de lentes."},
                    {"Cirugía Refractiva", "Especialidad quirúrgica para corregir problemas de refracción."},
                    {"Retina", "Especialidad que se dedica al diagnóstico y tratamiento de enfermedades de la retina."},
                    {"Glaucoma", "Especialidad centrada en el diagnóstico y tratamiento del glaucoma."},
                    {"Enfermera", "Especialidad que se encarga de la atención y cuidado del paciente."}
            };

            for (String[] especialidadData : especialidadesData) {
                String nombre = especialidadData[0];
                String descripcion = especialidadData[1];
                if (especialidadRepository.findByNombre(nombre).isEmpty()) {
                    Especialidad especialidad = new Especialidad(nombre, descripcion);
                    especialidadRepository.save(especialidad);
                    System.out.println("Especialidad creada: " + nombre);
                }
            }
        };
    }
}