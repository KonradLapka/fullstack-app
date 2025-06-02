package com.example.pasir_lapka_konrad.service;

import com.example.pasir_lapka_konrad.dto.MembershipDto;
import com.example.pasir_lapka_konrad.model.Group;
import com.example.pasir_lapka_konrad.model.Membership;
import com.example.pasir_lapka_konrad.model.User;
import com.example.pasir_lapka_konrad.repository.GroupRepository;
import com.example.pasir_lapka_konrad.repository.MembershipRepository;
import com.example.pasir_lapka_konrad.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public List<Membership> getGroupMembers(Long groupId) {
        return membershipRepository.findByGroupId(groupId);
    }

    public Membership addMember(MembershipDto membershipDTO) {
        User user = userRepository.findByEmail(membershipDTO.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + membershipDTO.getUserEmail()));

        Group group = groupRepository.findById(membershipDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy o ID: " + membershipDTO.getGroupId()));

        boolean alreadyMember = membershipRepository.findByGroupId(group.getId()).stream()
                .anyMatch(membership -> membership.getUser().getId().equals(user.getId()));

        if (alreadyMember) {
            throw new IllegalStateException("Użytkownik jest już członkiem tej grupy.");
        }

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        return membershipRepository.save(membership);
    }

    public void removeMember(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Członkostwo już istnieje"));

        User currentUser = getCurrentUser();
        User groupOwner = membership.getGroup().getOwner();

        if (!currentUser.getId().equals(groupOwner.getId())) {
            throw new SecurityException("Tylko właściciel grupy może usuwać członków");
        }

        membershipRepository.deleteById(membershipId);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika: " + email));
    }
}
