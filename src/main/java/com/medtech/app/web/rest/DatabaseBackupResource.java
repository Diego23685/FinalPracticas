package com.medtech.app.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/database")
public class DatabaseBackupResource {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseBackupResource.class);

    @Value("${spring.datasource.username:medtechApp}")
    private String dbUser;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    @GetMapping("/backup")
    public ResponseEntity<Resource> createBackup() {
        try {
            File tempFile = File.createTempFile("medtech_backup_", ".sql");

            ProcessBuilder pb = new ProcessBuilder(
                "docker",
                "exec",
                "-e",
                "PGPASSWORD=" + dbPassword,
                "medtechapp-postgresql-1",
                "pg_dump",
                "-U",
                dbUser,
                "medtechApp"
            );
            pb.redirectOutput(tempFile);
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return ResponseEntity.internalServerError().build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"medtech_backup.sql\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(tempFile.length())
                .body(resource);
        } catch (Exception e) {
            LOG.error("Error al generar backup", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<String> restoreBackup(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Archivo vacío");
        }
        try {
            Path tempPath = Files.createTempFile("restore_", ".sql");
            file.transferTo(tempPath.toFile());

            new ProcessBuilder("docker", "cp", tempPath.toString(), "medtechapp-postgresql-1:/tmp/restore.sql").start().waitFor();

            ProcessBuilder pb = new ProcessBuilder(
                "docker",
                "exec",
                "-e",
                "PGPASSWORD=" + dbPassword,
                "medtechapp-postgresql-1",
                "psql",
                "-U",
                dbUser,
                "-d",
                "medtechApp",
                "-f",
                "/tmp/restore.sql"
            );
            Process process = pb.start();
            int exitCode = process.waitFor();

            Files.deleteIfExists(tempPath);

            if (exitCode == 0) {
                return ResponseEntity.ok("Base de datos restaurada correctamente");
            } else {
                return ResponseEntity.internalServerError().body("Fallo al ejecutar restauración");
            }
        } catch (Exception e) {
            LOG.error("Error al restaurar backup", e);
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }
    }
}
