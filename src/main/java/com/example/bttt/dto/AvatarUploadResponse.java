package com.example.bttt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarUploadResponse {
    private Boolean success;
    private String message;
    private String avatarUrl;
    private String publicId;
    private Long userId;
}
