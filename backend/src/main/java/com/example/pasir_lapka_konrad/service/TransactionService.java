package com.example.pasir_lapka_konrad.service;

import com.example.pasir_lapka_konrad.dto.BalanceDto;
import com.example.pasir_lapka_konrad.dto.TransactionDto;
import com.example.pasir_lapka_konrad.exception.ExceptionConstraints;
import com.example.pasir_lapka_konrad.model.Transaction;
import com.example.pasir_lapka_konrad.model.TransactionType;
import com.example.pasir_lapka_konrad.model.User;
import com.example.pasir_lapka_konrad.repository.TransactionRepository;
import com.example.pasir_lapka_konrad.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<Transaction> getAllTransactions() {
        User user = getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionConstraints.TRANSACTION_NOT_FOUND.formatted(id)));
    }

    public Transaction updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionConstraints.TRANSACTION_NOT_FOUND.formatted(id)));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji tej transakcji");
        }

        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDto.getType()));
        transaction.setTags(transactionDto.getTags());
        transaction.setNotes(transactionDto.getNotes());

        return transactionRepository.save(transaction);
    }

    public Transaction createTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDto.getType()));
        transaction.setTags(transactionDto.getTags());
        transaction.setNotes(transactionDto.getNotes());
        transaction.setUser(getCurrentUser());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionConstraints.TRANSACTION_NOT_FOUND.formatted(id)));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do usunięcia tej transakcji");
        }

        transactionRepository.delete(transaction);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono zalogowanego użytkownika"));
    }

    public BalanceDto getUserBalance(User user, Float days) {
        List<Transaction> userTransactions;
        if (days == null) {
            userTransactions = transactionRepository.findAllByUser(user);
        } else {
            LocalDateTime timestamp = LocalDateTime.now();
            long seconds = (long) (days * 24 * 60 * 60);
            LocalDateTime desiredTimestamp = timestamp.minusSeconds(seconds);

            userTransactions = transactionRepository.findAllByUserAndTimestampAfter(user, desiredTimestamp);
        }

        double income = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new BalanceDto(income, expense, income - expense);
    }
}
