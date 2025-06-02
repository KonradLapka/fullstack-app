package com.example.pasir_lapka_konrad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    public Transaction(Double amount, TransactionType type, String tags, String notes, User user) {
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.notes = notes;
        this.timestamp = LocalDateTime.now();
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String tags;
    private String notes;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
