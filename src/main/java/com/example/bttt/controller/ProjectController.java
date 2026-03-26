package com.example.bttt.controller;

import com.example.bttt.dto.ApiResponse;
import com.example.bttt.dto.ProjectRequest;
import com.example.bttt.dto.ProjectResponse;
import com.example.bttt.service.ProjectService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid @RequestBody ProjectRequest request) {
        log.info("Create project: {}", request.getName());
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Project created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        log.info("Get project by id: {}", id);
        ProjectResponse response = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/member/{userId}")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectByMember(@PathVariable Long userId) {
        log.info("Get project member by id: {}", userId);
        List<ProjectResponse> response = projectService.getProjectByMember(userId);
        System.out.println("project " + response.get(0).getName());
        System.out.println("userId" + userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        log.info("Get all projects");
        List<ProjectResponse> response = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        log.info("Update project: {}", id);
        ProjectResponse response = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        log.info("Delete project: {}", id);
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Project deleted successfully", null));
    }
}
