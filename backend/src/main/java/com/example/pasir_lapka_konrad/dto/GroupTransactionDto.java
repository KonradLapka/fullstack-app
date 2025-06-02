package com.example.pasir_lapka_konrad.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupTransactionDto {
    private Long groupId;
    private Double amount;
    private String type;
    private String title;
    private List<Long> selectedUserIds;
}
