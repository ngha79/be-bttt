package com.example.bttt.service;

import com.example.bttt.dto.ProjectMemberSimpleResponse;
import com.example.bttt.dto.ProjectRequest;
import com.example.bttt.dto.ProjectResponse;
import com.example.bttt.entity.Project;
import com.example.bttt.exception.BadRequestException;
import com.example.bttt.exception.ResourceNotFoundException;
import com.example.bttt.repository.ProjectMemberRepository;
import com.example.bttt.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final ModelMapper modelMapper;

    public ProjectResponse createProject(ProjectRequest request) {
        // Validate name is required for creation
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Project name is required");
        }

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        project = projectRepository.save(project);
        log.info("Project created: {}", project.getId());
        return mapToResponse(project);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return mapToResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectByMember(Long userId) {
        return projectRepository.findProjectsByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        project.setUpdatedAt(System.currentTimeMillis());

        project = projectRepository.save(project);
        log.info("Project updated: {}", id);
        return mapToResponse(project);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        projectRepository.delete(project);
        log.info("Project deleted: {}", id);
    }

    private ProjectResponse mapToResponse(Project project) {
        List<ProjectMemberSimpleResponse> members = projectMemberRepository
                .findByProjectId(project.getId())
                .stream()
                .map(member -> ProjectMemberSimpleResponse.builder()
                        .id(member.getId())
                        .userId(member.getUser().getId())
                        .username(member.getUser().getUsername())
                        .role(member.getRole().toString())
                        .build())
                .collect(Collectors.toList());

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .taskCount(project.getTasks().size())
                .members(members)
                .build();
    }
}
