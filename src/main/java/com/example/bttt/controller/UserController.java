package com.example.bttt.controller;

import com.example.bttt.dto.*;
import com.example.bttt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        log.info("Get user by id: {}", id);
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        log.info("Get user by username: {}", username);
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        log.info("Get all users");
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        log.info("Create user by manager");
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {
        log.info("Update user by id: {}", id);
        UserResponse response = userService.updateUserById(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Upload avatar cho user
     * POST /api/users/{id}/avatar
     */
    @PostMapping("/{id}/avatar")
    public ResponseEntity<ApiResponse<AvatarUploadResponse>> uploadAvatar(
            @PathVariable Long id,
            @RequestParam(value = "file") MultipartFile file) {
        try {
            log.info("Upload avatar for user: {}, file: {}", id, file.getOriginalFilename());

            if (file.isEmpty()) {
                throw new IllegalArgumentException("File không được rỗng");
            }

            AvatarUploadResponse response = userService.uploadAvatar(id, file);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IOException e) {
            log.error("Lỗi upload avatar: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400,e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.error("Lỗi validate file: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400,e.getMessage()));
        }
    }

        @PostMapping("/change-password/{userId}")
    public ResponseEntity<ApiResponse<Map<String, String>>> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest, @PathVariable Long userId) {
        log.info("Change password user: {}", userId);
        userService.changePassword(userId,changePasswordRequest);
        return ResponseEntity.ok(
                ApiResponse.success(Map.of("message", "Đổi mật khẩu thành công"))
        );
    }

    /**
     * Xóa avatar của user
     * DELETE /api/users/{id}/avatar
     */
    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<ApiResponse<String>> deleteAvatar(@PathVariable Long id) {
        log.info("Delete avatar for user: {}", id);
        userService.deleteUserAvatar(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa avatar thành công"));
    }
}