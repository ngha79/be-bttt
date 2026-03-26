package com.example.bttt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberSimpleResponse {
    private Long id;
    private Long userId;
    private String username;
    private String role;
}
