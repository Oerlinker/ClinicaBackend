package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/backup")
public class BackupController {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BackupController(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(produces = "application/sql")
    public ResponseEntity<StreamingResponseBody> downloadBackup() {
        StreamingResponseBody stream = out -> {
            Writer writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

            List<String> tables = jdbcTemplate.queryForList(
                    "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'",
                    String.class
            );
            for (String table : tables) {
                writer.write("-- -----------------------------\n");
                writer.write("-- Table: " + table + "\n");
                writer.write("-- -----------------------------\n");
                writer.write("DROP TABLE IF EXISTS \"" + table + "\" CASCADE;\n");


                List<Map<String,Object>> cols = jdbcTemplate.queryForList(
                        "SELECT column_name, data_type FROM information_schema.columns WHERE table_schema='public' AND table_name=? ORDER BY ordinal_position",
                        table
                );
                writer.write("CREATE TABLE \"" + table + "\" (\n");
                for (int i = 0; i < cols.size(); i++) {
                    Map<String,Object> c = cols.get(i);
                    String name = (String)c.get("column_name");
                    String type = (String)c.get("data_type");
                    writer.write("  \"" + name + "\" " + type + (i < cols.size()-1 ? ",\n" : "\n"));
                }
                writer.write(");\n\n");

                List<Map<String,Object>> rows = jdbcTemplate.queryForList("SELECT * FROM \"" + table + "\"");
                for (Map<String,Object> row : rows) {
                    String colsList = row.keySet().stream()
                            .map(k -> "\"" + k + "\"")
                            .collect(Collectors.joining(", "));
                    String vals = row.values().stream().map(v -> {
                        if (v == null) return "NULL";
                        if (v instanceof Number) return v.toString();
                        String s = v.toString().replace("'", "''");
                        return "'" + s + "'";
                    }).collect(Collectors.joining(", "));
                    writer.write("INSERT INTO \"" + table + "\" (" + colsList + ") VALUES (" + vals + ");\n");
                }
                writer.write("\n");
            }
            writer.flush();
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/sql"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("backup.sql").build());
        return new ResponseEntity<>(stream, headers, HttpStatus.OK);
    }
}