package com.abbys.tms.data.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Min(value = 6,message = "Password must have minimum of 6 characters")
    @Max(value = 10,message = "Password must have maximum of 10 characters")
    private String password;
    @NotBlank(message = "Role is required")
    private String role;
    private Boolean enabled;
}
