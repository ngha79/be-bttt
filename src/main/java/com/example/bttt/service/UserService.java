package com.example.bttt.service;

import com.example.bttt.dto.AvatarUploadResponse;
import com.example.bttt.dto.ChangePasswordRequest;
import com.example.bttt.dto.UserRequest;
import com.example.bttt.dto.UserResponse;
import com.example.bttt.entity.User;
import com.example.bttt.enums.Role;
import com.example.bttt.exception.BadRequestException;
import com.example.bttt.exception.ResourceNotFoundException;
import com.example.bttt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return modelMapper.map(user, UserResponse.class);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new BadRequestException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new BadRequestException("Email đã được sử dụng!");
        }

        User user = new User();
        BeanUtils.copyProperties(userRequest, user);

        String encodedPassword = passwordEncoder.encode("123456");
        user.setPassword(encodedPassword);

        long currentTime = System.currentTimeMillis();
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);
        System.out.println(user.toString());
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user = userRepository.save(user);
        log.info("Đã tạo người dùng mới với ID: {}", user.getId());

        return mapToResponse(user);
    }

    @Transactional
    public UserResponse updateUserById(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        BeanUtils.copyProperties(userRequest, user, getNullPropertyNames(userRequest));

        user.setId(id);
        user.setUpdatedAt(System.currentTimeMillis());

        User updatedUser = userRepository.save(user);
        log.info("User updated: {}", id);
        return mapToResponse(updatedUser);
    }

    @Transactional
    public AvatarUploadResponse uploadAvatar(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Map<String, Object> uploadResult = cloudinaryService.uploadAvatar(file);

        if (user.getAvatarPublicId() != null && !user.getAvatarPublicId().isEmpty()) {
            cloudinaryService.deleteAvatar(user.getAvatarPublicId());
        }

        user.setAvatar((String) uploadResult.get("url"));
        user.setAvatarPublicId((String) uploadResult.get("publicId"));
        user.setUpdatedAt(System.currentTimeMillis());

        userRepository.save(user);

        AvatarUploadResponse response = new AvatarUploadResponse();
        response.setSuccess(true);
        response.setMessage("Upload avatar thành công");
        response.setAvatarUrl((String) uploadResult.get("url"));
        response.setPublicId((String) uploadResult.get("publicId"));
        response.setUserId(userId);

        log.info("Avatar uploaded for user: {}", userId);
        return response;
    }

    @Transactional
    public void deleteUserAvatar(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getAvatarPublicId() != null && !user.getAvatarPublicId().isEmpty()) {
            cloudinaryService.deleteAvatar(user.getAvatarPublicId());
        }

        user.setAvatar(null);
        user.setAvatarPublicId(null);
        user.setUpdatedAt(System.currentTimeMillis());

        userRepository.save(user);
        log.info("Avatar deleted for user: {}", userId);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }

    @Transactional
    public Boolean changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Mật khẩu hiện tại không chính xác!");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException("Mật khẩu mới phải khác mật khẩu cũ!");
        }

        if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 6) {
            throw new BadRequestException("Mật khẩu phải có ít nhất 6 ký tự!");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        user.setUpdatedAt(System.currentTimeMillis());

        userRepository.save(user);
        log.info("Đổi mật khẩu thành công cho user: {}", userId);

        return true;
    }
}