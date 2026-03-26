package com.example.bttt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank(message = "Tiêu đề công việc không được để trống")
    @Size(min = 1, max = 100, message = "Tiêu đề phải từ 1 đến 100 ký tự")
    private String title;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @NotNull(message = "ID dự án là bắt buộc")
    private Long projectId;

    @NotNull(message = "ID người thực hiện là bắt buộc")
    private Long userId;

    @NotNull(message = "Hạn chót (Deadline) không được để trống")
    private Long deadline;
}