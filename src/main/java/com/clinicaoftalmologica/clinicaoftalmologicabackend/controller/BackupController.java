package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

    import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.BackupService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.core.io.Resource;
    import org.springframework.core.io.UrlResource;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    import org.springframework.web.server.ResponseStatusException;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;

    @RestController
    @RequestMapping("/api/admin/backup")
    @PreAuthorize("hasAuthority('ADMIN')")
    public class BackupController {

        private static final Logger logger = LoggerFactory.getLogger(BackupController.class);

        @Autowired
        private BackupService backupService;

        @PostMapping("/create")
        public ResponseEntity<Resource> createAndDownloadBackup() {
            Path backupFilePath = null;
            try {
                backupFilePath = backupService.createBackup();
                Resource resource = new UrlResource(backupFilePath.toUri());

                if (resource.exists() && resource.isReadable()) {

                    logger.info("Ofreciendo backup para descarga: {}", resource.getFilename());

                    MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");


                    return ResponseEntity.ok()
                            .contentType(contentType)
                            .headers(headers)
                            .body(resource);
                } else {
                    logger.error("No se pudo leer el archivo de backup generado: {}", backupFilePath);
                    if (backupFilePath != null) {
                        try { Files.deleteIfExists(backupFilePath); } catch (IOException e) { logger.error("Error limpiando archivo ilegible {}", backupFilePath, e); }
                    }
                    throw new RuntimeException("No se pudo leer el archivo de backup: " + (backupFilePath != null ? backupFilePath.toString() : "null"));
                }
            } catch (Exception e) {
                logger.error("Error al crear o descargar el backup: {}", e.getMessage(), e);
                if (backupFilePath != null) {
                    try {
                        Files.deleteIfExists(backupFilePath);
                    } catch (IOException ioException) {
                        logger.error("Error al intentar limpiar el archivo temporal {}: {}", backupFilePath, ioException.getMessage());
                    }
                }
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear o descargar el backup", e);
            }
        }
    }