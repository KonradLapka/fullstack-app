package com.example.pasir_lapka_konrad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDto {

    @NotNull(message = "Kwota nie może być pusta")
    @Min(value = 1, message = "Kwota musi być większa niż 0")
    private Double amount;


    @NotNull(message = "Typ transakcji nie może być pusty")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Zły typ transakcji")
    private String type;

    @Size(max = 50, message = "Tagi nie mogą przekraczać 50 znaków")
    private String tags;

    @Size(max = 255, message = "Notatka może mieć maksymalnie 255 znaków")
    private String notes;
}
