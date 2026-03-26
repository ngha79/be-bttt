package com.example.bttt.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload ảnh avatar lên Cloudinary
     */
    public Map<String, Object> uploadAvatar(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File không được rỗng");
        }

        // Kiểm tra loại file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File phải là ảnh (JPEG, PNG, GIF, WebP)");
        }

        // Kiểm tra kích thước file (max 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("Kích thước file không được vượt quá 5MB");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "avatars",
                            "resource_type", "auto",
                            "quality", "auto",
                            "width", 500,
                            "height", 500,
                            "crop", "fill"
                    )
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", uploadResult.get("secure_url"));
            response.put("publicId", uploadResult.get("public_id"));
            response.put("size", uploadResult.get("bytes"));

            log.info("Upload avatar thành công: {} ({})",
                    uploadResult.get("public_id"),
                    file.getOriginalFilename());
            return response;

        } catch (IOException e) {
            log.error("Lỗi upload avatar: ", e);
            throw new IOException("Lỗi upload avatar: " + e.getMessage());
        }
    }

    /**
     * Xóa avatar cũ từ Cloudinary
     */
    public boolean deleteAvatar(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            return true;
        }

        try {
            Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            boolean success = deleteResult.get("result").equals("ok");

            if (success) {
                log.info("Xóa avatar thành công: {}", publicId);
            } else {
                log.warn("Không thể xóa avatar: {}", publicId);
            }
            return success;

        } catch (Exception e) {
            log.error("Lỗi xóa avatar: {}", publicId, e);
            return false;
        }
    }
}