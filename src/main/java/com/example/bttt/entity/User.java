package com.example.bttt.entity;

import com.example.bttt.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(exclude = {"tasks", "projectMembers"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(unique = true)
    private String email;

    @Column()
    private String phone;

    @Column()
    private String avatar;

    @Column(name = "avatar_public_id")
    private String avatarPublicId;

    @Column()
    private String address;

    @Column()
    private String gender;

    @Column()
    private Long dateOfBirth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProjectMember> projectMembers = new HashSet<>();

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Long createdAt = System.currentTimeMillis();

    @Column(nullable = false)
    @Builder.Default
    private Long updatedAt = System.currentTimeMillis();
}
