package com.example.bttt.service;

import com.example.bttt.dto.TaskRequest;
import com.example.bttt.dto.TaskResponse;
import com.example.bttt.entity.Project;
import com.example.bttt.entity.Task;
import com.example.bttt.entity.User;
import com.example.bttt.enums.TaskStatus;
import com.example.bttt.exception.BadRequestException;
import com.example.bttt.exception.ResourceNotFoundException;
import com.example.bttt.repository.ProjectRepository;
import com.example.bttt.repository.TaskRepository;
import com.example.bttt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public TaskResponse createTask(TaskRequest request) {
        // Validate project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.getProjectId()));

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        // Validate deadline is in future
        if (request.getDeadline() <= System.currentTimeMillis()) {
            throw new BadRequestException("Deadline must be in the future");
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .project(project)
                .user(user)
                .deadline(request.getDeadline())
                .status(TaskStatus.TODO)
                .build();

        task = taskRepository.save(task);
        log.info("Task created: {}", task.getId());
        return mapToResponse(task);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return mapToResponse(task);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByUserId(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByProjectId(Long projectId) {
        // Verify project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        // Cannot update if already DONE
        if (task.getStatus() == TaskStatus.DONE) {
            throw new BadRequestException("Cannot update task status because task is already DONE");
        }

        // Validate status transition
        validateStatusTransition(task.getStatus(), newStatus);

        task.setStatus(newStatus);
        task.setUpdatedAt(System.currentTimeMillis());
        task = taskRepository.save(task);
        log.info("Task status updated: {} -> {}", id, newStatus);
        return mapToResponse(task);
    }

    public TaskResponse assignTaskToUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        task.setUser(user);
        task.setUpdatedAt(System.currentTimeMillis());
        task = taskRepository.save(task);
        log.info("Task {} assigned to user {}", taskId, userId);
        return mapToResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (task.getStatus() == TaskStatus.DONE) {
            throw new BadRequestException("Cannot update task because status is DONE");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getDeadline() != null && request.getDeadline() > System.currentTimeMillis()) {
            task.setDeadline(request.getDeadline());
        }

        task.setUpdatedAt(System.currentTimeMillis());
        task = taskRepository.save(task);
        log.info("Task updated: {}", id);
        return mapToResponse(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        taskRepository.delete(task);
        log.info("Task deleted: {}", id);
    }

    private void validateStatusTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        switch (currentStatus) {
            case TODO:
                if (newStatus != TaskStatus.IN_PROGRESS) {
                    throw new BadRequestException("From TODO, task can only move to IN_PROGRESS");
                }
                break;
            case IN_PROGRESS:
                if (newStatus != TaskStatus.DONE) {
                    throw new BadRequestException("From IN_PROGRESS, task can only move to DONE");
                }
                break;
            case DONE:
                throw new BadRequestException("Cannot change status of completed task");
        }
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .userId(task.getUser().getId())
                .username(task.getUser().getUsername())
                .projectId(task.getProject().getId())
                .projectName(task.getProject().getName())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
