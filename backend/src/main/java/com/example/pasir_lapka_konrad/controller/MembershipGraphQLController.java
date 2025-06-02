package com.example.pasir_lapka_konrad.controller;

import com.example.pasir_lapka_konrad.dto.GroupResponseDto;
import com.example.pasir_lapka_konrad.dto.MembershipDto;
import com.example.pasir_lapka_konrad.dto.MembershipResponseDto;
import com.example.pasir_lapka_konrad.model.Group;
import com.example.pasir_lapka_konrad.model.Membership;
import com.example.pasir_lapka_konrad.model.User;
import com.example.pasir_lapka_konrad.repository.GroupRepository;
import com.example.pasir_lapka_konrad.repository.MembershipRepository;
import com.example.pasir_lapka_konrad.service.MembershipService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MembershipGraphQLController {

    private final MembershipService membershipService;
    private final MembershipRepository membershipRepository;
    private final GroupRepository groupRepository;

    public MembershipGraphQLController(MembershipService membershipService, MembershipRepository membershipRepository, GroupRepository groupRepository) {
        this.membershipService = membershipService;
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
    }

    @QueryMapping
    public List<MembershipResponseDto> groupMembers(@Argument Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy o ID: " + groupId));

        return membershipRepository.findByGroupId(group.getId()).stream()
                .map(membership -> new MembershipResponseDto(
                        membership.getId(),
                        membership.getUser().getId(),
                        membership.getGroup().getId(),
                        membership.getUser().getEmail()
                )).toList();
    }

    @MutationMapping
    public MembershipResponseDto addMember(@Valid @Argument MembershipDto membershipDTO) {
        Membership membership = membershipService.addMember(membershipDTO);
        return new MembershipResponseDto(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGroup().getId(),
                membership.getUser().getEmail()
        );
    }

    @QueryMapping
    public List<GroupResponseDto> myGroups() {
        User user = membershipService.getCurrentUser();
        return groupRepository.findByMemberships_User(user).stream()
                .map(group -> new GroupResponseDto(
                        group.getId(),
                        group.getName(),
                        group.getOwner().getId()
                )).toList();
    }
}
