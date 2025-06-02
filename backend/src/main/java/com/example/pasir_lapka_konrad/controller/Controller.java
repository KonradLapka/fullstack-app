package com.example.pasir_lapka_konrad.controller;

import com.example.pasir_lapka_konrad.dto.InfoResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/api/info")
    public ResponseEntity<InfoResponseDto> getInfo() {
        String name = "Aplikacja Budżetowa";
        String version = "1.0";
        String message = "Witaj w aplikacji budżetowej stworzonej ze Spring Boot!";
        return ResponseEntity.ok(new InfoResponseDto(name, version, message));
    }
}
