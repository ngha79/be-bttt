package com.example.bttt.controller;

import com.example.bttt.dto.ApiResponse;
import com.example.bttt.dto.TaskRequest;
import com.example.bttt.dto.TaskResponse;
import com.example.bttt.enums.TaskStatus;
import com.example.bttt.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request) {
        log.info("Create task: {}", request.getTitle());
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Task created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        log.info("Get task by id: {}", id);
        TaskResponse response = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks() {
        log.info("Get all tasks");
        List<TaskResponse> response = taskService.getAllTasks();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByUserId(@PathVariable Long userId) {
        log.info("Get tasks by user id: {}", userId);
        List<TaskResponse> response = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByProjectId(@PathVariable Long projectId) {
        log.info("Get tasks by project id: {}", projectId);
        List<TaskResponse> response = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByStatus(@PathVariable String status) {
        log.info("Get tasks by status: {}", status);
        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
        List<TaskResponse> response = taskService.getTasksByStatus(taskStatus);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        log.info("Update task: {}", id);
        TaskResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @PathVariable Long id,
            @PathVariable String status) {
        log.info("Update task status: {} -> {}", id, status);
        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
        TaskResponse response = taskService.updateTaskStatus(id, taskStatus);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTaskToUser(
            @PathVariable Long taskId,
            @PathVariable Long userId) {
        log.info("Assign task {} to user {}", taskId, userId);
        TaskResponse response = taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        log.info("Delete task: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success(200, "Task deleted successfully", null));
    }
}
