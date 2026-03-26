package com.example.bttt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberRequest {
    @NotNull(message = "Project ID không để trống")
    private Long projectId;

    @NotNull(message = "User ID không để trống")
    private Long userId;

    private String role;
}
