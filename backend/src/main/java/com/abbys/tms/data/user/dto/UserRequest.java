package com.abbys.tms.data.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Invalid Email")
    private String email;
    @Size(min = 6, max = 10, message = "Password must be between 6 and 10 characters")
    private String password;
    @NotBlank(message = "Role is required")
    private String role;
    private Boolean enabled;
}
