package com.abbys.tms.config;

import com.abbys.tms.data.user.entity.Role;
import com.abbys.tms.data.user.entity.User;
import com.abbys.tms.data.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepo _repo;
    private final PasswordEncoder _passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(!_repo.existsByEmail("admin@tms.com")) {
            User user = User.builder()
                    .name("admin")
                    .email("admin@tms.com")
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .password(_passwordEncoder.encode("admin@123"))
                    .build();
            _repo.save(user);
        }
    }
}
