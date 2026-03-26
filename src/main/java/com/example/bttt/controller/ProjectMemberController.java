package com.example.bttt.controller;

import com.example.bttt.dto.ApiResponse;
import com.example.bttt.dto.ProjectMemberRequest;
import com.example.bttt.dto.ProjectMemberResponse;
import com.example.bttt.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-members")
@Slf4j
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @PutMapping
    public ResponseEntity<ApiResponse<ProjectMemberResponse>> addMember(@Valid @RequestBody ProjectMemberRequest request) {
        log.info("Adding member to project: {}", request.getProjectId());
        ProjectMemberResponse response = projectMemberService.addMember(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Member added successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectMemberResponse>> getMemberById(@PathVariable Long id) {
        log.info("Get project member by id: {}", id);
        ProjectMemberResponse response = projectMemberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<ProjectMemberResponse>>> getProjectMembers(@PathVariable Long projectId) {
        log.info("Get members of project: {}", projectId);
        List<ProjectMemberResponse> response = projectMemberService.getProjectMembers(projectId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ProjectMemberResponse>>> getUserProjects(@PathVariable Long userId) {
        log.info("Get projects of user: {}", userId);
        List<ProjectMemberResponse> response = projectMemberService.getUserProjects(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/role/{role}")
    public ResponseEntity<ApiResponse<ProjectMemberResponse>> updateMemberRole(
            @PathVariable Long id,
            @PathVariable String role) {
        log.info("Update member {} role to {}", id, role);
        ProjectMemberResponse response = projectMemberService.updateMemberRole(id, role);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removeMemberById(@PathVariable Long id) {
        log.info("Remove member: {}", id);
        projectMemberService.removeMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Member removed successfully", null));
    }

    @DeleteMapping("/project/{projectId}/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        log.info("Remove user {} from project {}", userId, projectId);
        projectMemberService.removeMember(projectId, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Member removed successfully", null));
    }
}
