package com.clinicaoftalmologica.clinicaoftalmologicabackend.config;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Rol;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initRoles(RolRepository rolRepository){
        return args -> {
            String[] roles = {"ADMIN", "DOCTOR", "PACIENTE", "EMPLEADO"};
            for(String roleName : roles){
                if(rolRepository.findByNombre(roleName).isEmpty()){
                    rolRepository.save(new Rol(roleName));
                    System.out.println("Rol creado: " + roleName);
                }
            }
        };
    }
}
