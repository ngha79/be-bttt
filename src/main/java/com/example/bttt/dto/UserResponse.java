package com.example.bttt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String gender;
    private String address;
    private String avatar;
    private String phone;
    private Long dateOfBirth;
    private String role;
    private Long createdAt;
    private Long updatedAt;
}
