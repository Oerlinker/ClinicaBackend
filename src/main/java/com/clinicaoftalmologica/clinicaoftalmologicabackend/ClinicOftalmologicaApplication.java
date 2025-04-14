package com.clinicaoftalmologica.clinicaoftalmologicabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.config.DotenvLoader;

@SpringBootApplication
public class ClinicOftalmologicaApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ClinicOftalmologicaApplication.class);
        app.addInitializers(new DotenvLoader());
        app.run(args);
    }
}