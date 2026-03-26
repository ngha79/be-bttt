package com.example.bttt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Tên tài khoản không để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không để trống")
    private String password;
}
