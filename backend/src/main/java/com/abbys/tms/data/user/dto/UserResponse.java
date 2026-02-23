package com.abbys.tms.data.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean enabled;
    private String createdAt;
    private String updatedAt;
}
