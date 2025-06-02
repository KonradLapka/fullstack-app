package com.example.pasir_lapka_konrad.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDto {

    @NotNull(message = "Email uzytkownika nie moze byc pusty")
    private String userEmail;

    @NotNull(message = "ID grupy nie może byc puste")
    private Long groupId;
}
