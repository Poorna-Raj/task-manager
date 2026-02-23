package com.abbys.tms.service;

import com.abbys.tms.data.user.dto.UserRequest;
import com.abbys.tms.data.user.dto.UserResponse;
import com.abbys.tms.data.user.entity.Role;
import com.abbys.tms.data.user.entity.User;
import com.abbys.tms.data.user.repository.UserRepo;
import com.abbys.tms.exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest req) {
        User user = User.builder()
                .name(req.getName())
                .role(this.stringToEnum(Role.class, req.getRole()))
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();
        return mapToDto(repo.save(user));
    }

    public UserResponse updateUser(Long userId, UserRequest req) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new NotFound("Invalid User for the given ID"));

        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(this.stringToEnum(Role.class,req.getRole()));
        user.setEnabled(req.getEnabled());

        return mapToDto(repo.save(user));
    }

    public UserResponse getUserById(Long userId) {
         User user = repo.findById(userId)
                .orElseThrow(() -> new NotFound("Invalid User for the given ID"));
         return mapToDto(user);
    }

    public List<UserResponse> getAllUsers(String role) {
        if(role.isEmpty()) {
            return repo.findAll().stream().map(this::mapToDto).toList();
        } else {
            return repo.findByRole(this.stringToEnum(Role.class,role)).stream().map(this::mapToDto).toList();
        }
    }

    public boolean deleteUserById(Long userId) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new NotFound("Invalid User for the given ID"));
        repo.delete(user);
        return true;
    }

    private UserResponse mapToDto(User save) {
        return UserResponse.builder()
                .id(save.getId())
                .email(save.getEmail())
                .name(save.getName())
                .createdAt(save.getCreatedAt().toString())
                .role(save.getRole().name())
                .updatedAt(save.getUpdatedAt().toString())
                .enabled(save.isEnabled())
                .build();
    }

    private <T extends Enum<T>> T stringToEnum(Class<T> enumClass,String value) {
        if(value == null)
            throw new IllegalArgumentException("Invalid Value for " + enumClass.getCanonicalName());

        try{
            return Enum.valueOf(enumClass,value);
        } catch(IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
