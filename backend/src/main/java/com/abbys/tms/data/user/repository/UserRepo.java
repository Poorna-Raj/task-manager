package com.abbys.tms.data.user.repository;

import com.abbys.tms.data.user.entity.Role;
import com.abbys.tms.data.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
}
