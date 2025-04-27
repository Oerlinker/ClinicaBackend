package com.clinicaoftalmologica.clinicaoftalmologicabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class ClinicOftalmologicaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicOftalmologicaApplication.class, args);
    }
}