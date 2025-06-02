package com.example.pasir_lapka_konrad.service;

import com.example.pasir_lapka_konrad.dto.GroupTransactionDto;
import com.example.pasir_lapka_konrad.dto.TransactionDto;
import com.example.pasir_lapka_konrad.model.Debt;
import com.example.pasir_lapka_konrad.model.Group;
import com.example.pasir_lapka_konrad.model.Membership;
import com.example.pasir_lapka_konrad.model.User;
import com.example.pasir_lapka_konrad.repository.DebtRepository;
import com.example.pasir_lapka_konrad.repository.GroupRepository;
import com.example.pasir_lapka_konrad.repository.MembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupTransactionService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final DebtRepository debtRepository;
    private final TransactionService transactionService;

    public void addGroupTransaction(GroupTransactionDto dto, User currentUser) {
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy"));

        List<Membership> members = membershipRepository.findByGroupId(group.getId());
        List<Long> selectedUserIds = dto.getSelectedUserIds();

        if (selectedUserIds.isEmpty() || members.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano żadnych użytkowników");
        }

        double amountPerUser = dto.getAmount() / selectedUserIds.size();

        for (Membership member : members) {
            User debtor = member.getUser();
            if (!debtor.getId().equals(currentUser.getId()) && selectedUserIds.contains(debtor.getId())) {
                Debt debt = new Debt();
                debt.setDebtor(debtor);
                debt.setCreditor(currentUser);
                debt.setGroup(group);
                debt.setAmount(amountPerUser);
                debt.setTitle(dto.getTitle());
                debtRepository.save(debt);
            }
        }

        String users = members.stream().map(Membership::getUser).map(User::getEmail).toList().toString();
        TransactionDto transactionDto = new TransactionDto(dto.getAmount(), dto.getType(), "Transakcja grupowa: " + dto.getTitle(), "Użytkownicy: " + users);
        transactionService.createTransaction(transactionDto);
    }
}
