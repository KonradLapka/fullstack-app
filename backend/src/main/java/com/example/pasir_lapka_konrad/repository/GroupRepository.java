package com.example.pasir_lapka_konrad.repository;

import com.example.pasir_lapka_konrad.model.Group;
import com.example.pasir_lapka_konrad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByMemberships_User(User user);
}
