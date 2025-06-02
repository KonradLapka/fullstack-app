package com.example.pasir_lapka_konrad.dto;

import lombok.Data;

@Data
public class BalanceDto {
    private double totalIncome;
    private double totalExpense;
    private double balance;

    public BalanceDto(double income, double expenses, double balance) {
        this.totalIncome = income;
        this.totalExpense = expenses;
        this.balance = balance;
    }
}
