package com.example.bttt.service;

import com.example.bttt.dto.ProjectMemberRequest;
import com.example.bttt.dto.ProjectMemberResponse;
import com.example.bttt.entity.Project;
import com.example.bttt.entity.ProjectMember;
import com.example.bttt.entity.User;
import com.example.bttt.enums.ProjectMemberRole;
import com.example.bttt.exception.BadRequestException;
import com.example.bttt.exception.ResourceNotFoundException;
import com.example.bttt.repository.ProjectMemberRepository;
import com.example.bttt.repository.ProjectRepository;
import com.example.bttt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectMemberResponse addMember(ProjectMemberRequest request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.getProjectId()));

        // Check if user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        // Check if user is already a member
        if (projectMemberRepository.existsByProjectIdAndUserId(request.getProjectId(), request.getUserId())) {
            throw new BadRequestException("User is already a member of this project");
        }

        // Set default role if not provided
        ProjectMemberRole role = ProjectMemberRole.MEMBER;
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            try {
                role = ProjectMemberRole.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role. Must be one of: OWNER, MANAGER, MEMBER, VIEWER");
            }
        }

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(role)
                .build();

        member = projectMemberRepository.save(member);
        log.info("User {} added to project {}", user.getId(), project.getId());

        return mapToResponse(member);
    }

    @Transactional(readOnly = true)
    public ProjectMemberResponse getMemberById(Long id) {
        ProjectMember member = projectMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectMember", "id", id));
        return mapToResponse(member);
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getProjectMembers(Long projectId) {
        // Verify project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        return projectMemberRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getUserProjects(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return projectMemberRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectMemberResponse updateMemberRole(Long id, String newRole) {
        ProjectMember member = projectMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectMember", "id", id));

        try {
            ProjectMemberRole role = ProjectMemberRole.valueOf(newRole.toUpperCase());
            member.setRole(role);
            member.setUpdatedAt(System.currentTimeMillis());
            member = projectMemberRepository.save(member);
            log.info("Project member {} role updated to {}", id, newRole);
            return mapToResponse(member);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Must be one of: OWNER, MANAGER, MEMBER, VIEWER");
        }
    }

    @Transactional
    public void removeMember(Long projectId, Long userId) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectMember", "projectId and userId", projectId + ", " + userId));

        projectMemberRepository.delete(member);
        log.info("User {} removed from project {}", userId, projectId);
    }

    @Transactional
    public void removeMemberById(Long id) {
        ProjectMember member = projectMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectMember", "id", id));

        projectMemberRepository.delete(member);
        log.info("Project member {} removed", id);
    }

    @Transactional(readOnly = true)
    public boolean isMemberOfProject(Long userId, Long projectId) {
        return projectMemberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    private ProjectMemberResponse mapToResponse(ProjectMember member) {
        return ProjectMemberResponse.builder()
                .id(member.getId())
                .projectId(member.getProject().getId())
                .userId(member.getUser().getId())
                .username(member.getUser().getUsername())
                .role(member.getRole().toString())
                .joinedAt(member.getJoinedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
