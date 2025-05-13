package com.clinicaoftalmologica.clinicaoftalmologicabackend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@SpringBootApplication
public class ClinicOftalmologicaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicOftalmologicaApplication.class, args);
    }
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/La_Paz"));
    }
}