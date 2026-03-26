package com.example.bttt.service;

import com.example.bttt.dto.AuthResponse;
import com.example.bttt.dto.LoginRequest;
import com.example.bttt.dto.RegisterRequest;
import com.example.bttt.dto.UserResponse;
import com.example.bttt.entity.User;
import com.example.bttt.enums.Role;
import com.example.bttt.exception.BadRequestException;
import com.example.bttt.exception.UnAuthorizedException;
import com.example.bttt.repository.UserRepository;
import com.example.bttt.util.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        Role role = Role.USER;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Must be USER or MANAGER");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        user = userRepository.save(user);
        log.info("User registered: {}", user.getUsername());

        String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UnAuthorizedException("User not found"));

            String token = jwtTokenProvider.generateToken(authentication);
            log.info("User logged in: {}", user.getUsername());

            UserResponse userResponse = modelMapper.map(user, UserResponse.class);

            return AuthResponse.builder()
                    .token(token)
                    .user(userResponse)
                    .build();
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            throw new UnAuthorizedException("Invalid username or password");
        }
    }
}
