package com.example.bttt.entity;

import com.example.bttt.enums.ProjectMemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectMemberRole role = ProjectMemberRole.MEMBER;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Long joinedAt = System.currentTimeMillis();

    @Column(nullable = false)
    @Builder.Default
    private Long updatedAt = System.currentTimeMillis();
}
