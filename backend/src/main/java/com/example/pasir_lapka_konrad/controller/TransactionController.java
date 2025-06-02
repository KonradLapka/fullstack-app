package com.example.pasir_lapka_konrad.controller;

import com.example.pasir_lapka_konrad.dto.TransactionDto;
import com.example.pasir_lapka_konrad.model.Transaction;
import com.example.pasir_lapka_konrad.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody @Valid TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDto));
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
