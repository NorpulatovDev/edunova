package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.*;
import com.ogabek.edunova.entity.User;
import com.ogabek.edunova.exception.BadRequestException;
import com.ogabek.edunova.exception.UnauthorizedException;
import com.ogabek.edunova.repository.UserRepository;
import com.ogabek.edunova.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder, 
                      JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
        
        if (!user.getActive()) {
            throw new UnauthorizedException("User account is inactive");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for user: {}", request.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        }
        
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        
        log.info("User logged in successfully: {}", request.getUsername());
        return new AuthResponse(accessToken, refreshToken);
    }
    
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }
        
        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new UnauthorizedException("Refresh token does not match");
        }
        
        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());
        
        log.info("Token refreshed for user: {}", username);
        return new AuthResponse(newAccessToken, refreshToken);
    }
    
    @Transactional
    public void logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        
        user.setRefreshToken(null);
        userRepository.save(user);
        
        log.info("User logged out: {}", username);
    }
    
    @Transactional
    public void register(LoginRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        userRepository.save(user);
        
        log.info("New user registered: {}", request.getUsername());
    }
}