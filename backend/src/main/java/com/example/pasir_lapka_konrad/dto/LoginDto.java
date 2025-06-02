package com.example.pasir_lapka_konrad.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "Email nie może byc pusty")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;
}
