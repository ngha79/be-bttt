package com.example.bttt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberResponse {
    private Long id;
    private Long projectId;
    private Long userId;
    private String username;
    private String role;
    private Long joinedAt;
    private Long updatedAt;
}
