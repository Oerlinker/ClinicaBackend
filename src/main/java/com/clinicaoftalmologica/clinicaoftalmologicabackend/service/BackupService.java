package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupService {

    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);


    @Value("${PGHOST}")
    private String dbHost;

    @Value("${PGPORT}")
    private String dbPort;

    @Value("${DATABASE_USERNAME}")
    private String dbUser;

    @Value("${DATABASE_PASSWORD}")
    private String dbPassword;

    @Value("${PGDATABASE}")
    private String dbName;

    public Path createBackup() throws IOException, InterruptedException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFileName = String.format("backup_%s_%s.sql", dbName, timestamp);
        Path backupFilePath = Paths.get(System.getProperty("java.io.tmpdir"), backupFileName); // Usar directorio temporal

        logger.info("Iniciando backup de la base de datos '{}' en {}", dbName, backupFilePath);

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.environment().put("PGPASSWORD", dbPassword);
        processBuilder.command(
                "pg_dump",
                "-h", dbHost,
                "-p", dbPort,
                "-U", dbUser,
                "-d", dbName,
                "-F", "c",
                "-f", backupFilePath.toString()
        );

        Process process = processBuilder.start();
        int exitCode = process.waitFor();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.error("pg_dump error: {}", line);
            }
        }

        if (exitCode == 0) {
            logger.info("Backup completado exitosamente: {}", backupFilePath);
            return backupFilePath;
        } else {
            logger.error("Error al crear el backup. Código de salida: {}", exitCode);
            Files.deleteIfExists(backupFilePath);
            throw new IOException("Falló la ejecución de pg_dump con código: " + exitCode);
        }
    }
}